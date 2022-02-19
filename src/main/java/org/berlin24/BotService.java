package org.berlin24;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BotService {
    private final String TOKEN = "";
    private final TelegramBot bot = new TelegramBot(TOKEN);

    public void initUpdateListener() {

        bot.setUpdatesListener(updates -> {

            for (Update update : updates) {

                if (update.message() != null) {
                    long chatId = update.message().chat().id();
                    String messageFromUser = update.message().text();
                    System.out.println(messageFromUser);
                    analyzeMessage(chatId, messageFromUser);

                }

            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void analyzeMessage(long chatId, String message) {
        if (message.equals("/start")) {
            initOnStart(chatId,message);
        } else if (message.equals("/help")) {
            initOnHelp(chatId);
        } else {
            initOnStart(chatId,message);
        }
    }

    private void initOnStart(long chatId, String message) {

        try {
            Document doc = Jsoup.connect("https://berlin24.ru").get();
            Elements webPage = doc.body().getElementsByClass("block-accent block-menu col-menu");
            List<String> links = webPage.select("a").eachAttr("abs:href");
            List<String> labels = webPage.select("a").eachText();



            HashMap<String,String> map = new HashMap<>();

            for (int i=0; i<labels.size(); i++) {
                map.put(labels.get(i), links.get(i));
            }

            Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                    new String[]{labels.get(0),labels.get(1),labels.get(2)},
                    new String[]{labels.get(3),labels.get(4),labels.get(5)},
                    new String[]{labels.get(6),labels.get(7),labels.get(8)},
                    new String[]{labels.get(9),labels.get(10),labels.get(11)},
                    new String[]{labels.get(12),labels.get(13),labels.get(14)},
                    new String[]{labels.get(15),labels.get(16),labels.get(17)},
                    new String[]{labels.get(18),labels.get(19),labels.get(20)},
                    new String[]{labels.get(21),labels.get(22),labels.get(23)},
                    new String[]{labels.get(24),labels.get(25),labels.get(26)},
                    new String[]{labels.get(27),labels.get(28),labels.get(29)},
                    new String[]{labels.get(30),labels.get(31),labels.get(32)},
                    new String[]{labels.get(33),labels.get(34),labels.get(35)},
                    new String[]{labels.get(36),labels.get(37),labels.get(38)}
            )
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true)
                    .selective(true);
            if (map.containsKey(message)) {
                Document docu = Jsoup.connect(map.get(message)).get();
                Elements catalog = docu.body().getElementsByClass("catalog");

                List<String> infos = catalog.select("tbody").eachText();
                String messageRaw = "";
                for (String info:infos) {

                    messageRaw+=info;
                    messageRaw+="\n";
                    messageRaw+="--------------------";
                    messageRaw+="\n";

                }

                if (messageRaw.length() < 4096) {
                    SendResponse execute = this.bot.execute(new SendMessage(chatId,messageRaw));
                    SendResponse execute2 = this.bot.execute(new SendMessage(chatId,"To continue searching tap /again"));
                } else {

                    String messageFirst = messageRaw.substring(0,messageRaw.length()/2);
                    String messageSecond = messageRaw.substring(messageRaw.length()/2);

                    SendResponse execute1 = this.bot.execute(new SendMessage(chatId,messageFirst));
                    SendResponse execute2 = this.bot.execute(new SendMessage(chatId,messageSecond));
                    SendResponse execute3 = this.bot.execute(new SendMessage(chatId,"To continue searching tap /again"));

                }
            } else if (message.equals("/start") || message.equals("/again")) {
                SendResponse execute = this.bot.execute(new SendMessage(chatId,"Berlin 24").replyMarkup(replyKeyboardMarkup));
            } else if (!map.containsKey(message)) {
                SendResponse execute = this.bot.execute(new SendMessage(chatId,"Please type /help or /start"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initOnHelp(long chatId) {
        this.bot.execute(new SendMessage(chatId, "! ! ! T A P ! ! ! \n\n\n /start"));
    }

}
