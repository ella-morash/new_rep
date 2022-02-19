package org.berlin24;

public class Bot {

    private final BotService service;

    public Bot(BotService service) {
        this.service = service;
    }

    public void initBot(){
        this.service.initUpdateListener();
    }

}
