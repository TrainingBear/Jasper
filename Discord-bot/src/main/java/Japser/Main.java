package Japser;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault("MTMyOTQyNTY2NTk1NDQxNDYwMg.GTMZbv.OpkQ3663b4EtUbosLZWd7PeYVnFhH2zrloStEI")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.listening("!jhelp"))
                .build();
        jda.addEventListener(new Action());
    }
}


