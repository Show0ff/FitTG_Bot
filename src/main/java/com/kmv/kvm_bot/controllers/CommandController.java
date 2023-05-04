package com.kmv.kvm_bot.controllers;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;

@Component
public class CommandController {

    public String startCommandReceived(String firstName) {
        return EmojiParser.parseToUnicode("Привет, " + firstName + ", я бот, который поможет тебе отслеживать свои калории" + " :blush: ");
    }

    public String startWithUserHowExist() {
        return EmojiParser.parseToUnicode("Ты уже зарегистрирован :smile: ");
    }


}
