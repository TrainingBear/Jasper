package me.jasper.jasperproject.Bazaar.Component;

import lombok.Getter;
import lombok.val;
import me.jasper.jasperproject.Bazaar.util.*;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Product implements Content, Serializable {
    @Serial
    private static final long serialVersionUID = -1234567987645320457L;

    @Getter private String product_name;
    @Getter private ItemStack product;
    @Getter private ItemStack prototype;
    private final BazaarStock stock;
    private String namespace;

    private final AtomicInteger atomicInteger = new AtomicInteger();
    @Getter Map<UUID, Map<Integer, Order>> sell_order = new HashMap<>();
    @Getter Map<UUID, Map<Integer, Order>> buy_order = new HashMap<>();

    public Product(ItemStack product, String product_name) {
        this(product, product_name, null);
    }
    public Product(@NotNull ItemStack product, String product_name, @Nullable NamespacedKey key) {
        this(product, product_name, key, 0, 0, 0, 0);
    }
    public Product(@NotNull ItemStack product, String product_name, @Nullable NamespacedKey key, int stock, float sell_price, float buy_price, float bank) {
        this.product = product;
        this.product_name = escapeRegex(product_name);
        prototype = product.clone();
        prototype.editMeta(e->{
            PersistentDataContainer pdc = e.getPersistentDataContainer();
            pdc.set(JKey.BAZAAR_PRODUCT, PersistentDataType.STRING, product_name);
            pdc.set(JKey.GUI_BORDER, PersistentDataType.BOOLEAN, true);
            pdc.set(JKey.BAZAAR_COMPONENT_TASK_ID, PersistentDataType.BYTE, TaskID.OPEN_PRODUCT_MENU);
        });
        this.namespace = key==null? null: key.toString();
        this.stock = new BazaarStock(stock, sell_price, buy_price, bank);
    }

    private String escapeRegex(String name){
        return name.replaceAll("([\\\\\\[\\]{}()*+?^$|])", "");
    }

    public NamespacedKey getKey(){
        return NamespacedKey.fromString(namespace);
    }

    public void setNamespace(@Nullable NamespacedKey key){
        this.namespace = key==null? null : key.toString();
    }

    public void update() {
        try{
            Order finalTopSupply = topSellOffer();
            Order finalTopDemand = topBuyOffer();
            this.prototype.editMeta(e -> {
                e.lore(List.of(
                        text("<!i><gold>sell price: <sell_price></gold>",
                                Placeholder.unparsed("sell_price", ""+finalTopSupply.getOffer())),
                        text("<!i><gold>buy price: <buy_price></gold>",
                                Placeholder.unparsed("buy_price", ""+ finalTopDemand.getOffer())),
                        text("<!i><dark_green>supply: <v>, demand: <v2></dark_green>",
                                Placeholder.unparsed("v", String.valueOf(getGlobalSupply())),
                                Placeholder.unparsed("v2", String.valueOf(getDemand()))
                        )
                ));
            });
        } catch (OrderException ex) {
            this.prototype.editMeta(e -> {
                e.lore(List.of(
                        text("<!i><gold>sell price: <sell_price></gold>",
                                Placeholder.unparsed("sell_price", stock ==null? "N/A": ""+stock.getSellOffer())),
                        text("<!i><gold>buy price: <buy_price></gold>",
                                Placeholder.unparsed("buy_price", stock ==null? "N/A":""+ stock.getBuyOffer())),
                        text("<!i><dark_green>supply: <v>, demand: <v2></dark_green>",
                                Placeholder.unparsed("v", String.valueOf(getGlobalSupply())),
                                Placeholder.unparsed("v2", String.valueOf(getDemand()))
                        )
                ));
            });
        }

    }

    private int getGlobalSupply(){
        int stock = 0;
        for (Map<Integer, Order> value : this.sell_order.values()) {
            for (Order order : value.values()) {
                stock+=order.getStock();
            }
        }
        return stock + this.stock.getStock();
    }

    private int getDemand() {
        int demand = 0;
        for (Map<Integer, Order> value : this.buy_order.values()) {
            for (Order order : value.values()) {
                demand+=order.getMax() - order.getGoods();
            }
        }
        return demand;
    }

    public Order topBuyOffer() throws OrderException {
        if(buy_order.isEmpty()) throw new OrderException("Using Bazaar because cant find top demand", stock);
        Order holder = null;
        float price = this.stock.getBuyOffer();
        for (Map<Integer, Order> value : buy_order.values()) {
            for (Order order : value.values()) {
                if(!order.isAvailable()) continue;
                float p = order.getOffer();
                if(p > price) {
                    holder = order;
                    price = p;
                }
            }
        }
        if(holder == null) throw new OrderException("Using Bazaar because cant find top demand", stock);
        return holder;
    }

    public Order topSellOffer() throws OrderException {
        if(buy_order.isEmpty())throw new OrderException("Using Bazaar because cant find top offer", stock);
        Order holder = null;
        float price = Float.MAX_VALUE;
        for (UUID uuid : buy_order.keySet()) {
            for (Order order : buy_order.get(uuid).values()) {
                if(order.getStock()<=0) continue;
                float p = order.getOffer();
                if(p < price) {
                    holder = order;
                    price = p;
                }
            }
        }
        if(holder == null) throw new OrderException("Using Bazaar because cant find top offer", stock);
        return holder;
    }

    public void createSellOrder(@NotNull Player seller, int amount, float sell_offer){
        int id = id();
        this.sell_order.computeIfAbsent(seller.getUniqueId(), k -> new HashMap<>()).put(
                id, Order.createSellOrder(id, seller, amount, sell_offer));
    }

    public void createBuyOrder(Player player, int ammount, float price){
        int id = id();
        try{
            this.buy_order.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(
                    id, Order.createBuyOrder(id, player, ammount, price));
        }catch (OrderException e){
            player.sendMessage(e.getMessage());
        }
    }

    public @Nullable Order claimSellOrder(@NotNull Player player, int index){
        Order order = this.sell_order.get(player.getUniqueId()).get(index);
        deposit(player, order.withdrawBank());
        if(order.isClaimed(true)) {
            this.sell_order.get(player.getUniqueId()).remove(index);
            return null;
        }
        return order;
    }

    public void cancelSellOrder(Player player, int index){
        Order order = claimSellOrder(player, index);
        if(order==null) return;
        val quantity = order.getStock();
        val capacity = getPlayerCapacity(player);
        val amount = Math.min(quantity, capacity);
        product.setAmount(order.claimSupply(amount));
        player.getInventory().addItem(product);
        if(order.isClaimed(true)) {
            this.sell_order.get(player.getUniqueId()).remove(index);
        }
    }

    public @Nullable Order claimBuyOrder(@NotNull Player player, int index){
        Order order = this.buy_order.get(player.getUniqueId()).get(index);
        val goods = order.getGoods();
        val capacity = getPlayerCapacity(player);
        val min = Math.min(goods, capacity);
        product.setAmount(order.claimGoods(min));
        player.getInventory().addItem(product);
        if(order.isClaimed(false)) {
            this.buy_order.remove(player.getUniqueId()).remove(index);
            return null;
        }
        return order;
    }

    public void cancelBuyOrder(@NotNull Player player, int index){
        Order order = claimBuyOrder(player, index);
        if(order==null) return;
        deposit(player, order.withdrawBank());
        this.buy_order.remove(player.getUniqueId()).remove(index);
    }

    public synchronized void instantSell(@NotNull Player seller){
        int ammount= 0;
        NamespacedKey key = getKey();
        for (ItemStack item : seller.getInventory().getStorageContents()){
            if (item==null) continue;
            if (key == null){
                if (item.isSimilar(product)){
                    ammount+=item.getAmount();
                }
            } else if(item.getItemMeta().getPersistentDataContainer().has(key)){
                ammount+=item.getAmount();
            }
        }
        instantSell(seller, ammount, new OrderResult(OrderResult.Type.SELL));
    }
    public synchronized OrderResult instantSell(Player seller, int amount, OrderResult result) {
        if(amount<=0) return result;
        try{
            Order order = topBuyOffer();
            int quantity = Math.min(order.getStock(), amount);
            NamespacedKey key = NamespacedKey.fromString(namespace);
            for (ItemStack item : seller.getInventory().getStorageContents()) {
                if (item == null) continue;
                if (key == null) {
                    if (!item.isSimilar(product)) {
                        continue;
                    }
                } else if (!item.getItemMeta().getPersistentDataContainer().has(key)) {
                    continue;
                }
                val value = item.getAmount();
                if(quantity < value) item.setAmount(value-quantity);
                else {
                    item.setAmount(0);
                    quantity-=value;
                }
                result.addCart(order.getBusinessman(), order, quantity);
                if(quantity>0) return instantSell(seller, quantity, result);
            }
        } catch (OrderException e){
            seller.sendMessage(e.getLog());
            BazaarStock substitute = e.getSubstitute();
            int quantity = Math.min(substitute.getStock(), amount);
            NamespacedKey key = NamespacedKey.fromString(namespace);
            for (ItemStack item : seller.getInventory().getStorageContents()) {
                if (item == null) continue;
                if (key == null) {
                    if (!item.isSimilar(product)) {
                        continue;
                    }
                } else if (!item.getItemMeta().getPersistentDataContainer().has(key)) {
                    continue;
                }
                val value = item.getAmount();
                if(quantity < value){
                    item.setAmount(value-quantity);
                    quantity = 0;
                } else {
                    item.setAmount(0);
                    quantity-=value;
                }
                result.addCart(stock, quantity);
                if(quantity>0) return instantSell(seller, quantity, result);
            }
        }
        return result;
    }
    public synchronized void instantBuy(@NotNull Player buyer) throws OrderException{
        int ammount= 0;
        NamespacedKey key = getKey();
        for (ItemStack item : buyer.getInventory().getStorageContents()){
            if (item==null) continue;
            if(key == null){
                if(item.isSimilar(product)){
                    ammount+=item.getAmount();
                }
            } else if(item.getItemMeta().getPersistentDataContainer().has(key)){
                ammount+=item.getAmount();
            }
        }
        instantBuy(buyer, ammount, new OrderResult(OrderResult.Type.BUY));
    }

    public synchronized OrderResult instantBuy(Player buyer, int ammount, OrderResult result) throws OrderException{
        if(ammount<=0) return result;
        try{
            Order order = topSellOffer();
            val stock = Math.min(ammount, order.getStock());
            ammount = getPlayerCapacity(buyer);
            if(ammount<=0) return result;

            val quantity = Math.min(stock, ammount);
            result.addCart(order.getBusinessman(), order, quantity);
            ammount-=quantity;
            if(ammount > 0) return instantBuy(buyer, ammount, result);
        }catch (OrderException e){
            BazaarStock substitute = e.getSubstitute();
            int stock = substitute.getStock();
            ammount = getPlayerCapacity(buyer);
            if(ammount<=0) return result;

            val quantity = Math.min(stock, ammount);
            result.addCart(substitute, quantity);
            ammount-=quantity;

            if(ammount > 0) throw new OrderException("Cant find offers, You are buying "+result.getItem()+" items ("+result.getBills()+" rupiah). click to continue");
        }
        return result;
    }

    /**
     * @return return player available slot
     */
    private int getPlayerAvailableSlot(@NotNull Player player){
        @Nullable ItemStack[] storageContents = player.getInventory().getStorageContents();
        int i = 0;
        for (@Nullable ItemStack item : storageContents) {
            if(item==null) i++;
        }
        return i;
    }

    /**
     *
     * @param player pelaku
     * @return return max item can player hold
     */
    private int getPlayerCapacity(@NotNull Player player){
        return this.product.getMaxStackSize() * getPlayerAvailableSlot(player);
    }

    private void deposit(Player player, float amount){
        Economy eco = JasperProject.getEconomy();
        eco.depositPlayer(player, amount);
        player.sendMessage(MessageEnum.BAZAAR.append(text("<blue> You have been recived <value> rupiah",
            Placeholder.component("value", Component.text(amount))
        )));
    }
    public static boolean withdraw(Player player, float amount){
        Economy eco = JasperProject.getEconomy();
        if(!hasPurse(player, amount)) return false;
        eco.withdrawPlayer(player, amount);
        return true;
    }

    public static boolean hasPurse(Player online_buyer, float price){
        Economy eco = JasperProject.getEconomy();
        Logger log = new Logger(online_buyer);
        if(!eco.has(online_buyer, price)){
            double morecoin = price - eco.getBalance(online_buyer);
            log.info(MessageEnum.BAZAAR.append(text("<red>You need <v> more coins to buy this item!</red>", Placeholder.unparsed("v", String.valueOf(morecoin)))));
            return false;
        }
        return true;
    }

    private static @NotNull Component text(String s, TagResolver... r){
        return MiniMessage.miniMessage().deserialize(s, r);
    }

    @Override
    public int getID() {
        return 113;
    }

    @Override
    public ItemStack getItem() {
        update();
        return prototype;
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
    
    private int id(){
        return atomicInteger.getAndIncrement();
    }

    public static Product fromString(String name){
        return ProductManager.getProductMap().get(name);
    }
}
