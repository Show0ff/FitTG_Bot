package com.kmv.kvm_bot.config;

import com.kmv.kvm_bot.services.TelegramBot;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    @Autowired
    TelegramBot telegramBot;


    @SneakyThrows
    @EventListener({ContextRefreshedEvent.class})
    public void  init() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        telegramBotsApi.registerBot(telegramBot);

    }

}
