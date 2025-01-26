package Japser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static Japser.Method.*;

public class Embedmessg {
    static Random random = new Random();
    //\\======================================================== METHOD 2 FOR EMBED MESSAGE =================================================================||
    //||------------------------------------------------------------------- HELP EMBED -------------------------------------------------------------||
    public static void helpembd(MessageReceivedEvent event){
        EmbedBuilder helpembd= new EmbedBuilder();
        helpmessagesembd(helpembd);
        event.getMessage().replyEmbeds(helpembd.build()).queue();
    }
    public static void helpembd(SlashCommandInteractionEvent event){
        EmbedBuilder helpembd= new EmbedBuilder();
        helpmessagesembd(helpembd);
        event.replyEmbeds(helpembd.build()).queue();
    }
    public static void helpembd(MessageReceivedEvent event, String helptype){
        EmbedBuilder helpembd= new EmbedBuilder();
        helpmessagesembd(helpembd,helptype);
        event.getMessage().replyEmbeds(helpembd.build()).queue();
    }
    public static void helpembd(SlashCommandInteractionEvent event,String helptype) {
        EmbedBuilder helpembd = new EmbedBuilder();
        helpmessagesembd(helpembd, helptype);
        event.replyEmbeds(helpembd.build()).queue();
    }
    private static void helpmessagesembd(EmbedBuilder helpembd){
        helpembd.setColor(0xff00b6);
        helpembd.setTitle("Jasper⛏Project Command list");
        helpembd.setDescription("command yang lainnya ntar di tambahin sama developer");
        helpembd.addField("⁉️ !jhelp [command]","Mendapatkan list/keterangan command",false);
        helpembd.addField("🔢 !math or =","Kalkulator matematika",false);
        helpembd.addField("🪙 !coinflip [head/tail]", "Melempar koin & mendarat diantara head/tail *Suffix opsional*",false);
        helpembd.addField("🎲 !diceroll [1-6]","Melempar dadu & menunjukkan angka diantara 1-6 *Suffix opsional*",false);
        helpembd.addField("🃏 !blackjack","Minigame blackjack *mungkin kartu dengan jumlah kurang akurat*",false);
        helpembd.addField("♟️ !chess [player2]","*Coming Soon*",false);
        helpembd.setFooter("Jangan spam command!");
    }
    private static void helpmessagesembd(EmbedBuilder helpembd,String helptype){
        helpembd.setTitle("Jasper⛏Project Help Command "+helptype);
        helpembd.setColor(0xff00b6);
        switch (helptype) {
            case "!jhelp","!jh","jhelp","jh" -> {
                helpembd.setDescription("⁉️ Untuk mengetahui list command");
                helpembd.addField("!jhelp [command]","Untuk mengetahui tentang command tersebut lebih lanjut",false);
            }
            case "!math", "math", "=" -> {
                helpembd.setDescription("🔢 Kalkulator matematika\n**List/daftar operator**:");
                helpembd.addField("[ `+` ]", "Penjumlahan", true);
                helpembd.addField("[ `-` ]", "Pengurangan", true);
                helpembd.addField("[ `*` ]", "Perkalian", true);
                helpembd.addField("[ `/` ]", "Pembagian", true);
                helpembd.addField("[ `;` ]", "Modulus/sisa", true);
                helpembd.addField("[ `(n)` ]", "Tanda kurung", true);
                helpembd.addField("[ `(a^(b))` ]", "Perpangkatan", true);
                helpembd.addField("[ `n!` ]", "Pemfaktorial", true);
                helpembd.addField("[ `sqrt(n)` ]", "Akar kuadrat", true);
                helpembd.addField("[ `cbrt(n)` ]", "Akar kubik", true);
                helpembd.addField("[ `pi/π` ]", "Pi (3.14~)", true);
                helpembd.addField("[ n`k` ]", "suffix, seribu(kilo/χίλιοι)", true);
                helpembd.addField("[ n`jt` ]", "suffix, sejuta", true);
                helpembd.addField("[ n`m` ]", "suffix, semilyar", true);
                helpembd.addField("[ n`t` ]", "suffix, setriliun", true);
                helpembd.setFooter("Beberapa operator tidak ada karena terlalu kompleks\nuntuk fungsi persentase [ % ] kemungkinan akan mengalami error");
            }
            case "!coinflip", "!cf", "coinflip", "cf" -> helpembd.setDescription("🪙 Melempar **coin**(virtual) lalu mendarat antara __Tail__(angka) dan __Head__(Kepala/gambar)\n- Jika ingin menebak, tambahkan spasi lalu 'head'/'h' atau 'tail'/'t'");
            case "!diceroll", "!dr", "diceroll", "dr" -> helpembd.setDescription("🎲 Melempar **dadu**(virtual) lalu menunjukkan angka antara __1__ *sampai* __6__\n- Jika ingin menebak, tambahkan spasi lalu pilih 1-6");
            case "!blackjack","blackjack","!bj","bj" -> {
                helpembd.setDescription("🃏 **Permainan kartu/blackjack**\n bertujuan jumlah nominal kartu **tidak** melebihi dari __21__ dan **melebihi** dari dealer\nberikut cara bermainnya:");
                helpembd.addField("Awal bermain","**Dealer** akan membagikan masing-masing 2 kartu, **dealer** akan __membuka__ satu kartunya",false);
                helpembd.addField("[Hit]","**Dealer** akan __memberikan__ **kamu** satu kartu",false);
                helpembd.addField("[Stand]","**Kamu** __diam__/__tidak mengambil kartu__, **dealer** akan mengambil kartu untuk dirinya bila perlu",false);
            }
            case "!chess","!ch","chess","ch" -> helpembd.setDescription("Coming Soon...");
            default -> {
                helpembd.setColor(0xab00ff);
                helpembd.setDescription("Jenis command tersebut tidak valid");
                helpembd.setFooter("atau jika kamu merasa bug silahkan lapor ke server ini!");
            }
        }
    }

    //||-------------------------------------------------------------------- MATH EMBED --------------------------------------------------------------||
    public static void Mathembd(MessageReceivedEvent event, String mathinput, String resultstr){
        mathinput=mathinput.replaceAll("\\*","×")
                .replaceAll("pi","π")
                .replaceAll("sqrt","√")
                .replaceAll("cbrt","∛")
                .replaceAll("/","÷")
                .replaceAll(" ","");
        EmbedBuilder mathembd = new EmbedBuilder();
        mathembd.setColor(0xff00b6);
        mathembd.setTitle("Kalkulator");
        mathembd.setDescription("Hasil dari: "+mathinput+"\n"+resultstr);
        event.getMessage().replyEmbeds(mathembd.build()).queue();
    }
    //||--------------------------------------------------------- CoinFLIP EMBED ------------------------------------------------------------||
    public static void coinflipembd(MessageReceivedEvent event, String coinfacing2){
        int time = random.nextInt(4,7);
        String emoji = (coinfacing2.equals("Head"))?"<:coinhead:1329435281656385618>":"<:cointail:1329435269111484469>";
        EmbedBuilder cfembd = new EmbedBuilder();
        cfembd.setColor(0x9d0070);
        cfembd.setTitle("Coin sedang berputar... <a:coinspin:1329435247523401738>");
        cfembd.setDescription("Coin menghadap ...");
        event.getMessage().replyEmbeds(cfembd.build()).queue(sentmessage ->{
            cfembd.setColor(0xff00b6);
            cfembd.setTitle("Coin sudah berputar!");
            cfembd.setDescription("Coin menghadap **"+coinfacing2+"** "+emoji);
            sentmessage.editMessageEmbeds(cfembd.build()).queueAfter(time, TimeUnit.SECONDS);
        });
    }
    public static void coinflipembd(MessageReceivedEvent event, String coinfacing2,String cfselect){
        int time = random.nextInt(4,7);
        String emoji = (coinfacing2.equals("Head"))?"<:coinhead:1329435281656385618>":"<:cointail:1329435269111484469>";
        EmbedBuilder cfgembd = new EmbedBuilder();
        if(cfselect.equals("Head")||cfselect.equals("Tail")){
            if(cfselect.equals(coinfacing2)){
                cfgembd.setColor(0x9d0070);
                cfgembd.setTitle("Coin sedang berputar... <a:coinspin:1329435247523401738>");
                cfgembd.setDescription("Coin menghadap ...");
                event.getMessage().replyEmbeds(cfgembd.build()).queue(sentmessage ->{
                    cfgembd.setColor(0xff00f9);
                    cfgembd.setTitle("Coin berhenti berputar!");
                    cfgembd.setDescription("**BENAR!** Coin menghadap **"+coinfacing2+"** "+emoji);
                    sentmessage.editMessageEmbeds(cfgembd.build()).queueAfter(time, TimeUnit.SECONDS);
                });
            }else {
                cfgembd.setColor(0x9d0070);
                cfgembd.setTitle("Coin sedang berputar... <a:coinspin:1329435247523401738>");
                cfgembd.setDescription("Coin menghadap ...");
                event.getMessage().replyEmbeds(cfgembd.build()).queue(sentmessage ->{
                    EmbedBuilder cfgembd2 = new EmbedBuilder();
                    cfgembd2.setColor(0xff0089);
                    cfgembd2.setTitle("Coin berhenti berputar!");
                    cfgembd2.setDescription("**KALAH!** Coin menghadap **"+coinfacing2+"** "+emoji);
                    sentmessage.editMessageEmbeds(cfgembd2.build()).queueAfter(time, TimeUnit.SECONDS);
                });
            }
        }else {
            cfgembd.setColor(0xab00ff);
            cfgembd.setTitle("Coinflip");
            cfgembd.setDescription("**Error!** Pilih 'Head' atau 'Tail'!");
            event.getMessage().replyEmbeds(cfgembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
        }
    }
    //||--------------------------------------------------------- DIceRoll EMBED ------------------------------------------------------------||
    public static void Drembd(MessageReceivedEvent event,int dicenumber){
        int time = random.nextInt(4,7);
        EmbedBuilder drembd = new EmbedBuilder();
        drembd.setColor(0x9d0070);
        drembd.setTitle("Dadu sedang di kocok...");
        drembd.setDescription("Dice menghadap angka ...");
        event.getMessage().replyEmbeds(drembd.build()).queue(sentmessage ->{
            drembd.setColor(0xff00b6);
            drembd.setTitle("Dadu sudah di kocok!");
            drembd.setDescription("Dice menghadap angka **"+dicenumber+"**");
            sentmessage.editMessageEmbeds(drembd.build()).queueAfter(time, TimeUnit.SECONDS);});
    }
    public static void Drembd(MessageReceivedEvent event,String drguessnum,int dicenum){
        int time = random.nextInt(4,7);
        EmbedBuilder drgembd = new EmbedBuilder();
        if(drguessnum.contains(",")||drguessnum.contains(".")){
            drgembd.setColor(0xab00ff); drgembd.setTitle("Diceroll");drgembd.setDescription("**TIDAK BISA DESIMAL!** Pilih angka 1-6");
            event.getMessage().replyEmbeds(drgembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
        }else if(!drguessnum.matches("[1-6]")){
            drgembd.setColor(0xab00ff); drgembd.setTitle("Diceroll");drgembd.setDescription("**Error!** Pilih angka 1-6");
            event.getMessage().replyEmbeds(drgembd.build()).queue(message -> message.delete().queueAfter(8,TimeUnit.SECONDS));
        }
        else{
            if(Integer.parseInt(drguessnum)==dicenum){
                drgembd.setColor(0x9d0070);
                drgembd.setTitle("Dadu sedang di kocok...");
                drgembd.setDescription("Dice menghadap angka ...");
                event.getMessage().replyEmbeds(drgembd.build()).queue(sentmessage->{
                    drgembd.setColor(0xff00f9);
                    drgembd.setTitle("Dadu sudah di kocok!");
                    drgembd.setDescription("**BENAR!** Dadu menghadap angka **"+dicenum+"**");
                    sentmessage.editMessageEmbeds(drgembd.build()).queueAfter(time,TimeUnit.SECONDS);});
            }else{
                drgembd.setColor(0x9d0070);
                drgembd.setTitle("Dadu sedang di kocok...");
                drgembd.setDescription("Dice menghadap angka ...");
                event.getMessage().replyEmbeds(drgembd.build()).queue(sentmessage->{
                    drgembd.setColor(0xff0089);
                    drgembd.setTitle("Dadu sudah di kocok!");
                    drgembd.setDescription("**SALAH!** Dadu menghadap angka **"+dicenum+"**");
                    sentmessage.editMessageEmbeds(drgembd.build()).queueAfter(time,TimeUnit.SECONDS);});
            }
        }
    }
    //||---------------------------------------------------------- BlackJack EMBED -----------------------------------------------------------||
    public static void bjembd(MessageReceivedEvent event){
        EmbedBuilder bjembd = new EmbedBuilder();
        bjembd.setColor(0xff00b6);
        bjembd.setTitle("**BlackJack** Game Start!");
        bjembd.setDescription("Just started, at "+BjMove+" move!");
        bjembd.getFields().clear();
        bjembd.addField("Dealer: `["+dealercard.getFirst()+"+?]`",dealercardpic2.charAt(0)+"+?",true);
        bjembd.addField(bjusername+": `["+yourtotalcard+"]`",yourcardpic2.toString(),true);
        bjembd.setFooter("");
        event.getMessage().replyEmbeds(bjembd.build())
            .addActionRow(Button.danger("BJHit","Hit"),Button.primary("BJStand","Stand")).queue(message -> {
                    bjembd.setColor(0x9d0070);
                    bjembd.setTitle("**BlackJack** Game End!");
                    bjembd.setDescription("Ended, at "+BjMove+" move!");
                    bjembd.getFields().clear();
                    bjembd.addField("Dealer: `["+dealercard.getFirst()+"+?]`",dealercardpic2.charAt(0)+"+?",true);
                    bjembd.addField(bjusername+": `["+yourtotalcard+"]`",yourcardpic2.toString(),true);
                    bjembd.setFooter("Waktu Habis!");
                    BJtimer = new Timer();
                    BJtimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                                message.editMessageEmbeds(bjembd.build()).setComponents().queue();
                                BJtimer.cancel();clearallbj();
                        }
                    },30000); // 30 detik
            });
    }
    private static void clearallbj(){//**************************** HAPUS SEMUA DATA BJ **********************
        yourcardpic.clear();yourcardpic2.setLength(0);yourcard.clear();yourtotalcard=0;
        dealercardpic.clear();dealercardpic2.setLength(0);dealercard.clear();dealertotalcard=0;
        BjMove=0;bjstart=false;
    }
    //||------------------------------------------------ ROCK PAPER SCISSORS EMBED --------------------------------------------------------||
    public static void directgbkembd(MessageReceivedEvent event,byte gbkcomputerchoice,String yourchoice){
        EmbedBuilder gbkembd = new EmbedBuilder();
        gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
        String computerchoice;
        switch (gbkcomputerchoice){
            case 1-> computerchoice = "🗿";
            case 2-> computerchoice= "📃";
            case 3-> computerchoice= "✂️";
            default-> computerchoice = "";
        }
        switch (yourchoice){
            case"batu"->yourchoice="🗿";
            case"gunting"->yourchoice="✂️";
            case"kertas"->yourchoice="📃";
            default -> yourchoice="";
        }
        if((yourchoice.equals("🗿") && computerchoice.equals("✂️"))||(yourchoice.equals("✂️") && computerchoice.equals("📃"))||(yourchoice.equals("📃") && computerchoice.equals("🗿"))){
            gbkembd.setColor(0xff00f9);
            gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
            gbkembd.addField("Computer:",computerchoice,true);
            gbkembd.addField(gbkplayer1username+":",yourchoice,true);
            gbkembd.setFooter("**MENANG**!");
            event.getMessage().replyEmbeds(gbkembd.build()).queue();
        }
        else if((yourchoice.equals("🗿") && computerchoice.equals("📃"))||(yourchoice.equals("✂️") && computerchoice.equals("🗿"))||(yourchoice.equals("📃") && computerchoice.equals("✂️"))){
            gbkembd.setColor(0xff0e70);
            gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
            gbkembd.addField("Computer:",computerchoice,true);
            gbkembd.addField(gbkplayer1username+":",yourchoice,true);
            gbkembd.setFooter("**KALAH**!");
            event.getMessage().replyEmbeds(gbkembd.build()).queue();
        }else{
            gbkembd.setColor(0xb47ca6);
            gbkembd.setDescription("Kamu bermain dengan Computer.\nHasil:");
            gbkembd.addField("Computer:",computerchoice,true);
            gbkembd.addField(gbkplayer1username+":",yourchoice,true);
            gbkembd.setFooter("**SERI**!");
            event.getMessage().replyEmbeds(gbkembd.build()).queue();
        }
    }
    public static void gbkembd(MessageReceivedEvent event) {
        if(gbkplayerid2.isEmpty()||gbkplayerid2.equals("1329425665954414602")){
            String computerchoice;
            switch (gbkcomputerchoice){
                case 1-> computerchoice = "🗿";
                case 2-> computerchoice= "📃";
                case 3-> computerchoice= "✂️";
                default-> computerchoice = "";
            }
            gbkembd.setColor(0xff00b6);
            gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
            gbkembd.setDescription("Kamu bermain dengan Computer.\nMemilih...");
            gbkembd.getFields().clear();
            gbkembd.addField("Computer:", obfuscated1emoji + obfuscated2emoji + obfuscated3emoji, true);
            gbkembd.addField(gbkplayer1username + ":", obfuscated2emoji + obfuscated3emoji + obfuscated1emoji, true);
            gbkembd.setFooter("");
            event.getChannel().sendMessageEmbeds(gbkembd.build()).addActionRow(
                    Button.secondary("GBKbatu", "🗿")
                    , Button.secondary("GBKgunting", "✂️")
                    , Button.secondary("GBKkertas", "📃")).queue(message -> {
                    gbkembd.setDescription("Kamu bermain dengan Computer\nHasil:");
                    gbkembd.getFields().clear();
                    gbkembd.addField("Computer:",computerchoice, true);
                    gbkembd.addField(gbkplayer1username + ":", obfuscated2emoji + obfuscated3emoji + obfuscated1emoji, true);
                    gbkembd.setFooter("Waktu Habis!");
                    GBKtimer = new Timer();
                    GBKtimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            message.editMessageEmbeds(gbkembd.build()).setComponents().queue();
                            GBKtimer.cancel();clearallgbk();
                        }
                    }, 10000); // 15 detik
            });
        }else {
            gbkembd.getFields().clear();
            gbkembd.setColor(0x9d0070);
            gbkembd.setTitle("Batu🗿 Gunting✂️ Kertas📃");
            gbkembd.setDescription("**Kamu bermain dengan user lain.**\nMenunggu keputusan "+gbkplayer2username+"...");
            gbkembd.setFooter("Jika tidak menerima dalam 18 detik maka dianggap tidak menerima");
            event.getChannel().sendMessageEmbeds(gbkembd.build()).addActionRow(
                    Button.success("GBKaccept", "Yes")
                    ,Button.danger("GBKdecline","No")).queue(message -> {
                gbkembd.setDescription(gbkplayer2username+" **tidak menjawab...**");
                gbkembd.setFooter("Waktu Habis!");
                GBKtimer = new Timer();
                GBKtimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        message.editMessageEmbeds(gbkembd.build()).setComponents().queue();
                        GBKtimer.cancel();clearallgbk();
                    }
                }, 18000); // 18 detik
            });
        }
    }
    private static void clearallgbk(){
        gbkplayerid1="";gbkplayerid2="";gbkplayer1username="";gbkplayer2username="";gbkcomputerchoice =-1;gbkplayer1choice=-1;gbkplayer2choice =-1;gbkstart = false;
    }
}
    //||-------------------------------------------------------------------------------------------------------------------------------------||