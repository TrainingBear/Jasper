package Japser;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;


import static Japser.Method.*;

public class Action extends ListenerAdapter {
    Random random = new Random();
    static String username;
    final static String adminid= "781098203746795531";
    //**************************************************************************************************************************************||
    //**************************************************************************************************************************************||
    @Override
    public void onReady(@NotNull ReadyEvent event){
        Guild guild = event.getJDA().getGuildById("1238981507192717402");
        if (guild != null) {
            guild.upsertCommand("jhelp", "Daftar `!` command").addOptions(
                    new OptionData(OptionType.STRING,"help_type","Penjelasan command lebih lanjut",false)
                            .addChoice("math","math")
                            .addChoice("coinflip","coinflip")
                            .addChoice("diceroll","diceroll")
                            .addChoice("blackjack","blackjack")
            ).queue();
        }
    }
    //**************************************************************************************************************************************||
    //**************************************************************************************************************************************||
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
                return;}
        //LIST VARIABEL
        String inputuser = event.getMessage().getContentRaw().toLowerCase();
        String userping = "<@" + event.getAuthor().getId() + ">";
        String botself = "<@"+event.getJDA().getSelfUser().getId()+">";
        username = event.getAuthor().getName();
        String displayname = event.getAuthor().getEffectiveName();
        String channelname = event.getChannel().getName();
        String iduser= event.getAuthor().getId();
        EmbedBuilder errormathembdmsarg = new EmbedBuilder();
        errormathembdmsarg.setColor(0xab00ff);
    if(event.isFromGuild()|| iduser.equals(adminid)){
        // ||------------------------- List command -----------------------------||
        if (inputuser.equals("!jhelp")||inputuser.equals("!jh")) {
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            helpcommand(event); //METHOD
        }
        else if (inputuser.startsWith("!jhelp ")||inputuser.startsWith("!jh ")) {
            String helptype = null;
            if(inputuser.startsWith("!jhelp ")){
                helptype = inputuser.substring(7);}
            else if(inputuser.startsWith("!jh ")){
                helptype = inputuser.substring(4);
            }
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            helpcommand(event,helptype);//METHOD
        }
        // ||------------------------- MATH -----------------------------||
        else if (inputuser.startsWith("!math ")) {
            System.out.print("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            mathcommand(inputuser,event); //METHOD
        }
        else if (inputuser.equals("!math")){
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            errormathembdmsarg.setTitle("Apakah kamu menggunakan command **!math** ?");errormathembdmsarg.setDescription("Kamu tidak menambahkan operasi matematikanya");
            event.getMessage().replyEmbeds(errormathembdmsarg.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
        }
        else if (inputuser.startsWith("= ")) {
            System.out.print("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            mathcommand(inputuser,event); //METHOD
        }
        else if (inputuser.equals("=")) {
            System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
            errormathembdmsarg.setTitle("Apakah kamu menggunakan command **=** ?");errormathembdmsarg.setDescription("Kamu tidak menambahkan operasi matematikanya");
            event.getMessage().replyEmbeds(errormathembdmsarg.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
        }
        // ||----------------------------- Coin flip ------------------------||
        else if (inputuser.equals("!coinflip")||inputuser.equals("!cf")){
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            coinflipcommand(event);} //METHOD
        else if (inputuser.startsWith("!coinflip ") || inputuser.startsWith("!cf ")) {
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            String cfguess = inputuser.startsWith("!coinflip ") ? "!coinflip " : "!cf ";
            String cfselect = inputuser.substring(cfguess.length());
            switch (cfselect){case"h"->cfselect="Head";case"t"-> cfselect ="Tail";}
            coinflipguesscommand(cfselect,event); //METHOD
        }
        //||------------------------------ Dice roll ---------------------------||
        else if (inputuser.equals("!diceroll") || inputuser.equals("!dr")){
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            dicerollcommand(event);} //METHOD
        else if (inputuser.startsWith("!diceroll ")||inputuser.startsWith("!dr ")) {
            System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
            String drguess = inputuser.startsWith("!diceroll ")?"!diceroll ":"!dr ";
            String drguessnum = inputuser.substring(drguess.length());
            dicerollguesscommand(drguessnum, event); //METHOD
        }
        //||-------------------------------- BlackJack game -------------------------------||
        else if (inputuser.startsWith("!blackjack")||inputuser.startsWith("!bj")){
            System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
            if(!bjstart){
                bjalloweduseridstr = event.getAuthor().getId();
                blackjackcommand(event, username);//METHOD
            }else{
                EmbedBuilder bjerrorembd = new EmbedBuilder();
                bjerrorembd.setColor(0xab00ff);
                bjerrorembd.setTitle("**ERROR!** BlackJack");
                bjerrorembd.setDescription("User lain sedang bermain...");
                event.getMessage().replyEmbeds(bjerrorembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
            }
        }
        //||-------------------------------- ROCK PAPER SCISSORS ---------------------------------||
        else if(inputuser.equals("!batu")||inputuser.equals("!gunting")||inputuser.equals("!kertas")||inputuser.equals("!b")||inputuser.equals("!g")||inputuser.equals("!k")){
            System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
            String yourchoice = inputuser.substring(1).toLowerCase();
            directgbkcommand(event,yourchoice);
        }
        else if(inputuser.startsWith("!gbk")){
            System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
            EmbedBuilder gbkerrorembd = new EmbedBuilder();
            gbkplayerid2 = "";
            if(!gbkstart){
                gbkplayerid1 = event.getAuthor().getId();
                gbkplayer1username = username;
                Pattern pattern = Pattern.compile("<@\\d+>");
                Matcher matcher = pattern.matcher(inputuser);
                if(matcher.find()){
                    gbkplayerid2 = matcher.group().replaceAll("[^0-9]", "");
                    User user = event.getJDA().retrieveUserById(gbkplayerid2).complete();
                    gbkplayer2username= user.getName();
                } // Taro id di long
                matcher.reset(); // Reset matcher untuk mencocokkan ulang
                byte count=0;
                while (matcher.find()) {
                    count++;
                    if(count==2) {
                        break;
                    }
                } //cari klo ada 2
                if(count==2) {
                    gbkerrorembd.setColor(0xab00ff);
                    gbkerrorembd.setTitle("**ERROR!** Batu🗿 Gunting✂️ Kertas📃");
                    gbkerrorembd.setDescription("Tolong input 1 user saja");
                    event.getMessage().replyEmbeds(gbkerrorembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
                } //klo 2?
                else {
                    if (gbkplayerid2.equals(gbkplayerid1)) {
                        gbkerrorembd.setColor(0xab00ff);
                        gbkerrorembd.setTitle("**ERROR!** Batu🗿 Gunting✂️ Kertas📃");
                        gbkerrorembd.setDescription("Setress main sama diri sendiri...");
                        event.getMessage().replyEmbeds(gbkerrorembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
                    }
                    else {
                        gbkcommand(event);
                    }
                }
            }else{
                gbkerrorembd.setColor(0xab00ff);
                gbkerrorembd.setTitle("**ERROR!** Batu🗿 Gunting✂️ Kertas📃");
                gbkerrorembd.setDescription("User lain sedang bermain...");
                event.getMessage().replyEmbeds(gbkerrorembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
            }
        }

        //||--------------------------------- CHESS, died --------------------------------||
        else if(inputuser.startsWith("!chess")||inputuser.startsWith("!ch")){
            System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
        }
        //||----------------------------------- Say ---------------------------------||
        else if(inputuser.startsWith("!say ")){
          System.out.println("(" + channelname + ").(" + displayname + ")(" + username + ") : " + inputuser);
          if(iduser.equals(adminid)) {

          }
       }
    }
    else{
        System.out.println("("+channelname+").("+displayname+")("+username+") : "+inputuser);
        if(inputuser.startsWith("!")||inputuser.startsWith("=")){
            event.getMessage().reply("tidak menerima command di private messages, silahkan input di server https://discord.com/invite/fbAZSd3Hf2").queue();
        }
    }
}

    //**************************************************************************************************************************************||
    //**************************************************************************************************************************************||
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        User userwhoclick = event.getUser();

        //||---------------------------------------------- BLACKJACK BUTTON SHIT -------------------------------------------||
        switch (buttonId){
            case "BJHit"->{
                if (userwhoclick.getId().equals(bjalloweduseridstr) || userwhoclick.getId().equals(adminid)) {
                    if (dealertotalcard < 17) {
                        dealercardshuffle();
                    }//***dealer random card add ***
                    yourcardshuffle();
                    if (yourtotalcard <= 21) {
                        BjMove++;
                        bjembd.setColor(0xff00b6);
                        bjembd.setTitle("**BlackJack** Ongoing!");
                        bjembd.getFields().clear();
                        bjembd.addField("Dealer: ```[" + dealercard.getFirst() + "+?]```", dealercardpic2.charAt(0) + "+?", true);
                        bjembd.addField(bjusername + ": ```[" + yourtotalcard + "]```", yourcardpic2.toString(), true);
                        bjembd.setDescription("Started, at " + BjMove + " move!");
                        bjembd.setFooter("");
                        event.editMessageEmbeds(bjembd.build()).queue();
                    }
                    else {
                        bjembd.setTitle("**BlackJack** Ongoing!");
                        bjembd.getFields().clear();
                        bjembd.addField("Dealer: `[" + dealertotalcard + "]`", dealercardpic2.toString(), true);
                        bjembd.addField(bjusername + ": `[" + yourtotalcard + "]`", yourcardpic2.toString(), true);
                        bjembd.setDescription("Ended, at " + BjMove + " move!");
                        if (dealertotalcard > 21) {
                            bjembd.setColor(0xb47ca6);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**BUST!** ");
                        }else{
                            bjembd.setColor(0xff0e70);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**KALAH!** ");
                        }
                        event.editMessageEmbeds(bjembd.build()).setComponents().queue();
                        clearallbj();
                    }
                } else {
                    event.reply("bukan lu yang main goblog").setEphemeral(true).queue();
                }
            }     //FOR BJ
            case "BJStand" ->{
                if (userwhoclick.getId().equals(bjalloweduseridstr) || userwhoclick.getId().equals(adminid)) {
                    while (dealertotalcard < 17) {
                        dealercardshuffle();
                    }
                    BjMove++;
                    bjembd.setColor(0xff00b6);
                    bjembd.setTitle("**BlackJack** Ongoing!");
                    bjembd.getFields().clear();
                    bjembd.addField("Dealer: `[" + dealertotalcard + "]`", dealercardpic2.toString(), true);
                    bjembd.addField(bjusername + ": `[" + yourtotalcard + "]`", yourcardpic2.toString(), true);
                    bjembd.setDescription("Ended, at " + BjMove + " move!");
                    if (yourtotalcard > 21 && dealertotalcard > 21) {
                        bjembd.setColor(0xb47ca6);
                        bjembd.setTitle("**BlackJack** Game End!");
                        bjembd.setFooter("**BUST!** ");
                        event.editMessageEmbeds(bjembd.build()).setComponents().queue();
                        clearallbj(); //METHOD
                    }
                    else if (yourtotalcard > dealertotalcard) {
                        if (yourtotalcard > 21) {
                            bjembd.setColor(0xff0e70);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**KALAH!** ");
                        } else {
                            bjembd.setColor(0xff00f9);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**MENANG!** ");
                        }
                        event.editMessageEmbeds(bjembd.build()).setComponents().queue();
                        clearallbj(); //METHOD
                    }
                    else if (yourtotalcard < dealertotalcard) {
                        if (dealertotalcard > 21) {
                            bjembd.setColor(0xff00f9);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**MENANG!** ");
                        } else {
                            bjembd.setColor(0xff0e70);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**KALAH!** ");
                        }
                        event.editMessageEmbeds(bjembd.build()).setComponents().queue();
                        clearallbj(); //METHOD
                    }
                    else {
                        if (yourcard.size() > dealercard.size()) {
                            bjembd.setColor(0xff0e70);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**KALAH!** \njumlah kartu kamu lebih banyak dari dealer");
                        } else if (yourcard.size() < dealercard.size()) {
                            bjembd.setColor(0xff00f9);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**MENANG!** \njumlah kartu kamu lebih banyak dari dealer");
                        } else {
                            bjembd.setColor(0xb47ca6);
                            bjembd.setTitle("**BlackJack** Game End!");
                            bjembd.setFooter("**BUST!** \njumlah kartu kamu sama dari dealer");
                        }
                        event.editMessageEmbeds(bjembd.build()).setComponents().queue();
                        clearallbj();
                    }
                } else {
                    event.reply("Bukan kamu yang main").setEphemeral(true).queue();
                }
            }
            case "GBKaccept" ->{
                if(userwhoclick.getId().equals(gbkplayerid2)){
                    GBKtimer.cancel();
                    gbkembd.setColor(0xff00b6);
                    gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
                    gbkembd.setDescription("Kamu bermain dengan "+gbkplayer2username+".\nMemilih...");
                    gbkembd.setFooter("");
                    gbkembd.getFields().clear();
                    gbkembd.addField(gbkplayer2username+":", obfuscated1emoji + obfuscated2emoji + obfuscated3emoji, true);
                    gbkembd.addField(gbkplayer1username + ":", obfuscated2emoji + obfuscated3emoji + obfuscated1emoji, true);
                    event.editMessageEmbeds(gbkembd.build()).setComponents(ActionRow.of(
                            Button.secondary("GBKbatu", "🗿")
                            , Button.secondary("GBKgunting", "✂️")
                            , Button.secondary("GBKkertas", "📃"))).queue(message -> {
                        GBKtimer = new Timer();
                        GBKtimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                gbkembd.setDescription("Kamu bermain dengan "+gbkplayer2username+"\nHasil:");
                                gbkembd.getFields().clear();
                                gbkembd.addField(gbkplayer2username+":", gbkplaceholder2lambda(), true);
                                gbkembd.addField(gbkplayer1username + ":", gbkplaceholder1lambda(), true);
                                gbkembd.setFooter("Waktu Habis!");
                                message.editOriginalEmbeds(gbkembd.build()).setComponents().queue();
                                GBKtimer.cancel();clearallgbk();
                            }
                        }, 15000); // 15 detik
                    });
                }
                else if (userwhoclick.getId().equals(gbkplayerid1)){
                    event.reply("Kamu tidak bisa memencet ini, hanya yang kamu request yang bisa").setEphemeral(true).queue();
                }
                else{
                    event.reply("Bukan kamu yang main").setEphemeral(true).queue();
                }
            }
            case "GBKdecline"->{
                if(userwhoclick.getId().equals(gbkplayerid2)){
                    GBKtimer.cancel();
                    gbkembd.setColor(0xff0089);
                    gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
                    gbkembd.setDescription(gbkplayer2username+" menolak... how sad ;l");
                    gbkembd.setFooter("");
                    event.editMessageEmbeds(gbkembd.build()).setComponents().queue(message-> message.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
                    clearallgbk();
                }
                else if (userwhoclick.getId().equals(gbkplayerid1)){
                    event.reply("Kamu tidak bisa memencet ini, hanya yang kamu request yang bisa").setEphemeral(true).queue();
                }
                else{
                    event.reply("Bukan kamu yang main").setEphemeral(true).queue();
                }
            }
            case "GBKbatu" ->{
                if(userwhoclick.getId().equals(gbkplayerid1)){
                    gbkplayer1choice = 1;
                }else if(userwhoclick.getId().equals(gbkplayerid2)){
                    gbkplayer2choice = 1;
                }
                gbkcheckstatus(event);
            }
            case "GBKkertas" ->{
                if(userwhoclick.getId().equals(gbkplayerid1)){
                    gbkplayer1choice = 2;
                }else if(userwhoclick.getId().equals(gbkplayerid2)){
                    gbkplayer2choice = 2;
                }
                gbkcheckstatus(event);
            }
            case "GBKgunting" ->{
                if(userwhoclick.getId().equals(gbkplayerid1)){
                    gbkplayer1choice = 3;
                }else if(userwhoclick.getId().equals(gbkplayerid2)){
                    gbkplayer2choice = 3;
                }
                gbkcheckstatus(event);
            }
        }
        //||----------------------------------------------------------------------------------------------------------------||
    }
    //||================================================== FOR BJ =====================================||
    private void dealercardshuffle() {
        int ratio = random.nextInt(1, 14);
        String matchesnum;
        if (ratio == 1) {
            dealercard.add(BjAceCard[random.nextInt(BjAceCard.length)]);
            if (dealercard.getFirst().equals(1)) {
                matchesnum = BjFaceCardSymAce[0];
            } else {
                matchesnum = BjFaceCardSymAce[1];
            }
            dealercardpic.add(matchesnum);
        } else if (ratio >= 2 && ratio <= 4) {
            dealercard.add(BjFaceCard[random.nextInt(BjFaceCard.length)]);
            matchesnum = BjFaceCardSym[random.nextInt(BjFaceCardSym.length)];
            dealercardpic.add(matchesnum);
        } else if (ratio >= 5) {
            dealercard.add(NumCard[random.nextInt(NumCard.length)]);
            matchesnum = String.valueOf(dealercard.getLast());
            dealercardpic.add(matchesnum);
        }
        dealertotalcard = 0; //************** re clear to readd **************
        for (int num : dealercard) {
            dealertotalcard += num;
        }
        dealercardpic2.setLength(0);
        for (int i = 0; i < dealercard.size(); i++) {
            dealercardpic2.append(dealercardpic.get(i));
            if (i < dealercard.size() - 1) {
                dealercardpic2.append("+"); // Tambahkan "+" setelah elemen, kecuali untuk elemen terakhir
            }
        }
    }
    private void yourcardshuffle () {
        int ratio = random.nextInt(1, 14);
        //**** your random card add *******
        String matchesnum;
        if (ratio == 1) {
            yourcard.add(BjAceCard[random.nextInt(BjAceCard.length)]);
            if (yourcard.getFirst().equals(1)) {
                matchesnum = BjFaceCardSymAce[0];
            } else {
                matchesnum = BjFaceCardSymAce[1];
            }
            yourcardpic.add(matchesnum);
        } else if (ratio >= 2 && ratio <= 4) {
            yourcard.add(BjFaceCard[random.nextInt(BjFaceCard.length)]);
            matchesnum = BjFaceCardSym[random.nextInt(BjFaceCardSym.length)];
            yourcardpic.add(matchesnum);
        } else if (ratio >= 5) {
            yourcard.add(NumCard[random.nextInt(NumCard.length)]);
            matchesnum = String.valueOf(yourcard.getLast());
            yourcardpic.add(matchesnum);
        }
        yourtotalcard = 0;//************** re clear to readd **************
        for (int num : yourcard) {
            yourtotalcard += num;
            yourcardpic2.setLength(0); //***** re clear card to seperate with + ******
            for (int i = 0; i < yourcard.size(); i++) {
                yourcardpic2.append(yourcardpic.get(i));
                if (i < yourcard.size() - 1) {
                    yourcardpic2.append("+"); // Tambahkan "+" setelah elemen, kecuali untuk elemen terakhir
                }
            }
        }
    }
    private void clearallbj(){//**************************** HAPUS SEMUA DATA BJ **********************
        yourcardpic.clear();yourcardpic2.setLength(0);yourcard.clear();yourtotalcard=0;
        dealercardpic.clear();dealercardpic2.setLength(0);dealercard.clear();dealertotalcard=0;
        BjMove=0;bjstart=false;BJtimer.cancel();
        gbkplayerid1="";gbkplayerid2="";
    }
    //||====================================== FOR ROCK PAPER SCISSORS =================================================||
    private static void clearallgbk(){
        gbkplayerid1="";gbkplayerid2="";gbkplayer1username="";gbkplayer2username="";gbkcomputerchoice =-1;gbkplayer1choice=-1;gbkplayer2choice =-1;gbkstart = false;GBKtimer.cancel();
    }
    private static String gbkplaceholdercomputerlambda(){
        if (gbkcomputerchoice == 1) {
            return "🗿";
        }else if(gbkcomputerchoice==2){
            return "📃";
        }else if (gbkcomputerchoice==3){
            return "✂️";
        }else{
            return obfuscated2emoji + obfuscated3emoji + obfuscated1emoji;
        }
    }
    private static String gbkplaceholder1lambda() {
        if (gbkplayer1choice == 1) {
            return "🗿";
        }else if(gbkplayer1choice==2){
            return "📃";
        }else if (gbkplayer1choice==3){
            return "✂️";
        }else{
            return obfuscated1emoji + obfuscated2emoji + obfuscated3emoji;
        }
    }
    private static String gbkplaceholder2lambda() {
        if (gbkplayer2choice == 1) {
            return "🗿";
        }else if(gbkplayer2choice==2){
            return "📃";
        }else if (gbkplayer2choice==3){
            return "✂️";
        }else{
            return obfuscated2emoji + obfuscated3emoji + obfuscated1emoji;
        }
    }
    private static void gbkcheckstatus(ButtonInteractionEvent event) {
        if (gbkplayer1choice > 0 && (!gbkplayerid2.isEmpty() && gbkplayer2choice < 0)) {
            gbkembd.setColor(0xff00b6);
            gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
            gbkembd.setDescription("Kamu bermain dengan " + gbkplayer2username + ".\nMemilih...");
            gbkembd.setFooter("");
            gbkembd.getFields().clear();
            gbkembd.addField(gbkplayer2username + ":", obfuscated1emoji + obfuscated2emoji + obfuscated3emoji, true);
            gbkembd.addField(gbkplayer1username + ":", obfuscated2emoji + obfuscated3emoji + obfuscated1emoji + "✅", true);
            event.editMessageEmbeds(gbkembd.build()).queue();
        }
        else if (gbkplayer1choice < 0 && (!gbkplayerid2.isEmpty() && gbkplayer2choice > 0)) {
            gbkembd.setColor(0xff00b6);
            gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
            gbkembd.setDescription("Kamu bermain dengan " + gbkplayer2username + ".\nMemilih...");
            gbkembd.setFooter("");
            gbkembd.getFields().clear();
            gbkembd.addField(gbkplayer2username + ":", obfuscated1emoji + obfuscated2emoji + obfuscated3emoji + "✅", true);
            gbkembd.addField(gbkplayer1username + ":", obfuscated2emoji + obfuscated3emoji + obfuscated1emoji, true);
            event.editMessageEmbeds(gbkembd.build()).queue();
        }
        else {
            //GOOD TO KNOW > 1= ROCK 2= PAPER 3= SCISSORS
            if((gbkplayer1choice==1&&(gbkplayer2choice==3||gbkcomputerchoice==3))
                ||(gbkplayer1choice==2&&(gbkplayer2choice==1||gbkcomputerchoice==1))
                ||(gbkplayer1choice==3&&(gbkplayer2choice==2||gbkcomputerchoice==2))) {
                if (gbkplayer2username!=null&&!gbkplayer2username.isEmpty()) {
                    gbkembd.setColor(0xff00b6);
                    gbkembd.setDescription("Kamu bermain dengan " + gbkplayer2username + "\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField(gbkplayer2username + ":", gbkplaceholder2lambda(), true);
                    gbkembd.setFooter(gbkplayer1username + " Menang!");
                } else {
                    gbkembd.setColor(0xff00f9);
                    gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField("Computer:",gbkplaceholdercomputerlambda(),true);
                    gbkembd.setFooter("MENANG!");
                }
                gbkembd.addField(gbkplayer1username + ":", gbkplaceholder1lambda(), true);
                event.editMessageEmbeds(gbkembd.build()).setComponents().queue();
                clearallgbk();
            }
            else if((gbkplayer1choice==1&&(gbkplayer2choice==2||gbkcomputerchoice==2))
                    ||(gbkplayer1choice==2&&(gbkplayer2choice==3||gbkcomputerchoice==3))
                    ||(gbkplayer1choice==3&&(gbkplayer2choice==1||gbkcomputerchoice==1))) {
                if (gbkplayer2username!=null&&!gbkplayer2username.isEmpty()) {
                    gbkembd.setColor(0xff00b6);
                    gbkembd.setDescription("Kamu bermain dengan " + gbkplayer2username + "\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField(gbkplayer2username + ":", gbkplaceholder2lambda(), true);
                    gbkembd.setFooter(gbkplayer2username + " Menang!");
                } else {
                    gbkembd.setColor(0xff0e70);
                    gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField("Computer:",gbkplaceholdercomputerlambda(),true);
                    gbkembd.setFooter("KALAH!");
                }
                gbkembd.addField(gbkplayer1username + ":", gbkplaceholder1lambda(), true);
                event.editMessageEmbeds(gbkembd.build()).setComponents().queue();
                clearallgbk();
            }else{
                if (!gbkplayer2username.isEmpty()) {
                    gbkembd.setDescription("Kamu bermain dengan " + gbkplayer2username + "\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField(gbkplayer2username + ":", gbkplaceholder2lambda(), true);
                    gbkembd.setFooter("SERI!");
                } else {
                    gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField("Computer:",gbkplaceholdercomputerlambda(),true);
                    gbkembd.setFooter("SERI!");
                }
                gbkembd.setColor(0xb47ca6);
                gbkembd.addField(gbkplayer1username + ":", gbkplaceholder1lambda(), true);
                event.editMessageEmbeds(gbkembd.build()).setComponents().queue();
                clearallgbk();
            }
        }
    }
    //**************************************************************************************************************************************||
    //**************************************************************************************************************************************||
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if (event.getName().equals("jhelp")){
            String helptype = event.getOption("help_type") != null ? Objects.requireNonNull(event.getOption("help_type")).getAsString() : "";
            if(!helptype.isEmpty()){
                helpcommand(event,helptype);
            }else{
                helpcommand(event);
            }
        }







    }
}