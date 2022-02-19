package org.berlin24;

public class BotApp {
    public static void main(String[] args) {
        Bot bot = new Bot(new BotService());
         bot.initBot();
    }
}
