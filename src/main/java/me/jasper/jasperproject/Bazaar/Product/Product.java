package me.jasper.jasperproject.Bazaar.Product;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.BazaarException;
import me.jasper.jasperproject.Bazaar.Component.MessageEnum;
import me.jasper.jasperproject.Bazaar.Component.TaskID;
import me.jasper.jasperproject.JasperProject;
import me.jasper.jasperproject.Util.ContainerMenu.Content;
import me.jasper.jasperproject.Util.JKey;
import me.jasper.jasperproject.Util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Product implements Content, Serializable {
    @Getter private String product_name;
    @Getter private ItemStack product;
    @Getter private ItemStack prototype;
    private String name/*space*/;
    private String key;

    private final AtomicInteger atomicInteger = new AtomicInteger();
    @Getter Map<UUID, Map<Integer, Order>> sell_order = new HashMap<>();
    @Getter Map<UUID, Map<Integer, Order>> buy_order = new HashMap<>();


    public Product(ItemStack product, String product_name, @Nullable NamespacedKey key) {
        this(product, product_name,key==null? null : key.getNamespace(),key==null? null : key.getKey());
        setNamespace(key);
    }
    public Product(ItemStack product, String product_name, String key) {
        this(product, product_name, JasperProject.getPlugin().getName().toLowerCase(), key);
        setNamespace(key);
    }
    public Product(ItemStack product, String product_name,String namespace, String key) {
        this.product = product;
        this.product_name = product_name;
        prototype = product.clone();
        prototype.editMeta(e->{
            PersistentDataContainer pdc = e.getPersistentDataContainer();
            pdc.set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name);
            pdc.set(JKey.GUI_BORDER, PersistentDataType.BOOLEAN, true);
            pdc.set(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE, TaskID.BUY);
        });
        setNamespace(namespace, key);
        try{
            this.update();
        } catch (BazaarException e){}

    }

    public NamespacedKey getKey(){
        return new NamespacedKey(this.name, this.key);
    }

    public void setNamespace(@Nullable NamespacedKey key){
        boolean isnull = key==null;
        String namespace = isnull? null: key.getNamespace();
        String keys = isnull? null: key.getKey();
        if(namespace.equals("minecraft")) {
            namespace=null;
            keys=null;
        }
        setNamespace(namespace, keys);
    }
    public void setNamespace(String key){
        setNamespace(JasperProject.getPlugin().getName().toLowerCase(), key);
    }
    public void setNamespace(String namespace, String key){
        this.name = namespace;
        this.key = key;
    }

    public void update() throws BazaarException{
        Order topSupply = topSellOffer();
        Order topDemand = topBuyOffer();
        this.prototype.editMeta(e -> {
            e.lore(List.of(
                    text("<!i><gold>sell price: <sell_price></gold>",
                            Placeholder.unparsed("sell_price", ""+topSupply.p)),
                    text("<!i><gold>buy price: <buy_price></gold>",
                            Placeholder.unparsed("buy_price", ""+topDemand.p)),
                    text("<!i><dark_green>supply: <v>, demand: <v2></dark_green>",
                            Placeholder.unparsed("v", String.valueOf(getSupply())),
                            Placeholder.unparsed("v2", String.valueOf(getDemand()))
                    )
            ));
        });
    }

    private int getSupply(){
        int stock = 0;
        for (Map<Integer, Order> value : this.sell_order.values()) {
            for (Order order : value.values()) {
                stock+=order.q;
            }
        }
        return stock;
    }

    private int getDemand(){
        int demand = 0;
        for (Map<Integer, Order> value : this.buy_order.values()) {
            for (Order order : value.values()) {
                demand+=order.q;
            }
        }
        return demand;
    }

    public @Nullable Order topBuyOffer() throws BazaarException{
        if(buy_order.isEmpty()) throw new BazaarException("No Supply!");

        Order holder = null;
        float price = Float.MIN_VALUE;

        for (UUID uuid : buy_order.keySet()) {
            for (Order order : buy_order.get(uuid).values()) {
                if(order.q<=0) continue;
                float p = order.p;
                if(p > price) {
                    holder = order;
                    price = p;
                }
            }
        }

        if(holder==null) throw new BazaarException("No Supply!");
        return holder;
    }

    public @Nullable Order topSellOffer() throws BazaarException{
        if(buy_order.isEmpty()) throw new BazaarException("No Demand");
        Order holder = null;
        float price = Float.MAX_VALUE;

        for (UUID uuid : buy_order.keySet()) {
            for (Order order : buy_order.get(uuid).values()) {
                if(order.q<=0) continue;
                float p = order.p;
                if(p < price) {
                    holder = order;
                    price = p;
                }
            }
        }
        if(holder==null) throw new BazaarException("No Supply!");
        return holder;
    }
    /**
     * @param best_offer best_offer
     * @param pelaku buyer
     * @param quantity total quantity
     * @param price price per unit
     * @param type TransactionTYpe
     * @return true if succes, either false
     */
    @Deprecated
    private boolean completePayment(Order best_offer, UUID pelaku, int quantity, float price, TransactionType type){
        Economy eco = JasperProject.getEconomy();
        price*=quantity;

        switch (type) {
            case INSTANT_SELL -> {
                Player online_seller = Bukkit.getPlayer(pelaku);
                eco.depositPlayer(online_seller, price);

                best_offer.setProfit(-price);
                best_offer.setGoods(quantity);

            }
            case INSTANT_BUY -> {
                if(!hasPurse(pelaku, price)) return false;

                Player online_buyer = Bukkit.getPlayer(pelaku);
                eco.withdrawPlayer(online_buyer, price);

                best_offer.setProfit(price);
                best_offer.setGoods(-quantity);
            }
        } return true;
    }

    private boolean hasPurse(UUID on_player, float price){
        return hasPurse(Bukkit.getPlayer(on_player), price);
    }
    private boolean hasPurse(Player online_buyer, float price){
        Economy eco = JasperProject.getEconomy();
        Logger log = new Logger(online_buyer);
        if(!eco.has(online_buyer, price)){
            double morecoin = price - eco.getBalance(online_buyer);
            log.info(MessageEnum.BAZAAR.append(text("<red>You need <v> more coins to buy this item!</red>", Placeholder.unparsed("v", String.valueOf(morecoin)))));
            return false;
        }
        return true;
    }

    public void createSellOrder(@NotNull Player seller, int ammount, float price){
        int id = this.atomicInteger.getAndDecrement();
        this.sell_order.computeIfAbsent(seller.getUniqueId(), k -> new HashMap<>()).put(id,
                new Order(
                        new Curve(ammount, price),
                        seller.getUniqueId(),
                        ammount,
                        0,
                        id
                ));
    }

    public void createBuyOrder(Player player, int ammount, float price){
        Economy eco = JasperProject.getEconomy();
        price*=ammount;
        if(!hasPurse(player, price)) return;

        int id = this.atomicInteger.getAndDecrement();
        this.buy_order.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(id,
            new Order(
                new Curve(ammount, price),
                player.getUniqueId(),
                0,
                (float) eco.withdrawPlayer(player, price).amount,
                    id
            ));
    }

    public void claimSellOrder(@NotNull Player player, int index){
        Economy eco = JasperProject.getEconomy();

        Order order = this.sell_order.get(player.getUniqueId()).get(index);
        float profit = order.bank;
        eco.depositPlayer(player, profit);
        order.setProfit(-profit);

        if(order.q<=0) this.sell_order.remove(player.getUniqueId());
    }

    public void cancelSellOrder(Player player, int index){
        claimSellOrder(player, index);
        Order order = this.sell_order.get(player.getUniqueId()).get(index);
        val quantity = order.q;
        val capacity = getPlayerAvailableSlot(player);

        val ammount = Math.min(quantity, capacity);
        product.setAmount(ammount);
        order.decreaseQuantity(ammount);
        player.getInventory().addItem(product);

        if(order.q <= 0) this.sell_order.remove(player.getUniqueId());
    }

    public void claimBuyOrder(@NotNull Player player, int index){
        val order = this.buy_order.get(player.getUniqueId()).get(index);
        val goods = order.goods;
        val capacity = getPlayerAvailableSlot(player) * product.getMaxStackSize();

        val min = Math.min(goods, capacity);
        order.setGoods(-min);
        product.setAmount(min);
        player.getInventory().addItem(product);

        if(order.goods <=0) this.buy_order.remove(player.getUniqueId());
    }

    public void cancelBuyOrder(Player player, int index){
        Economy eco = JasperProject.getEconomy();

        Order order = this.buy_order.get(player.getUniqueId()).get(index);
        claimBuyOrder(player, index);
        val bank = order.bank;
        eco.depositPlayer(player, bank);

        if(order.q <= 0) this.buy_order.remove(player.getUniqueId());
    }

    public synchronized void instantSell(Player seller){
        int ammount= 0;

        NamespacedKey key = null;
        if(this.name!=null && this.key!=null){
            key = new NamespacedKey(this.name, this.key);
        }

        for (ItemStack item : seller.getInventory().getStorageContents()){
            if (item==null) continue;
            if(key == null){
                if(item.isSimilar(product)){
                    ammount+=item.getAmount();
                }
            } else if(item.getItemMeta().getPersistentDataContainer().has(key)){
                ammount+=item.getAmount();
            }
        }
        instantSell(seller, ammount);
    }
    public synchronized void instantSell(Player seller, int ammount) throws BazaarException {
        Economy eco = JasperProject.getEconomy();

        Order order;
        try {
            order = topBuyOffer();
        } catch (BazaarException e) {
            throw new RuntimeException(e);
        }
        int quantity = Math.min(order.q, ammount);

        NamespacedKey key = null;
        if(this.name!=null && this.key!=null){
            key = new NamespacedKey(this.name, this.key);
        }
        for (ItemStack item : seller.getInventory().getStorageContents()) {
            if (item == null) continue;
            if (key == null) {
                if (!item.isSimilar(product)) {
                    continue;
                }
            } else if (!item.getItemMeta().getPersistentDataContainer().has(key)) {
                continue;
            }

            val value2 = item.getAmount();
            val value = quantity % value2;
            if (quantity < value2) {
                item.setAmount(value2 - quantity);
                break;
            }
            item.setAmount(value);
            quantity -= value;

            val value3 = item.getAmount();
            order.setGoods(value3);
            order.setProfit(-value3 * order.p);

            eco.depositPlayer(seller, value3 * order.p);
        }
        if (ammount > 0) {
            instantSell(seller, ammount);
        }
    }

    public synchronized void instantBuy(Player buyer, int ammount) throws BazaarException{
        if(ammount<=0) return;

        Order top;
        try{
            top = topSellOffer();
        } catch (BazaarException e) {
            throw new RuntimeException(e);
        }

        val offer_quantity = Math.min(ammount, top.q);
        val max_buyer_quantity = getPlayerAvailableSlot(buyer) * this.product.getMaxStackSize();
        if(max_buyer_quantity<=0) return;
        val quantity = Math.min(offer_quantity, max_buyer_quantity);
        if (!hasPurse(buyer, top.p)) return;
        ammount-=quantity;

        top.setProfit(quantity*top.p);
        top.setGoods(-quantity);

        if(ammount > 0){
            instantBuy(buyer, ammount);
        }
    }

    private int getPlayerAvailableSlot(@NotNull Player player){
        @Nullable ItemStack[] storageContents = player.getInventory().getStorageContents();
        int i = 0;
        for (@Nullable ItemStack item : storageContents) {
            if(item==null) i++;
        }
        return i;
    }

    private @NotNull Component text(String s, TagResolver... r){
        return MiniMessage.miniMessage().deserialize(s, r);
    }

    @Override
    public int getID() {
        return 113;
    }

    @Override
    public ItemStack getItem() {
        return prototype;
    }

    public enum TransactionType implements Serializable{
        INSTANT_SELL,
        INSTANT_BUY
    }

    public byte[] serialize(){
        try(
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                BukkitObjectOutputStream oout = new BukkitObjectOutputStream(out)
        ) {
            oout.writeObject(this);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
