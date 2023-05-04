package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.utill.NamesOfMainMenuButtons;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainKeyBoardService {

    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyBoardRows = new ArrayList<>();

        KeyboardRow keyBoardRow = new KeyboardRow();
        keyBoardRow.add(NamesOfMainMenuButtons.SHOW_MY_DATA);
        keyBoardRow.add(NamesOfMainMenuButtons.SET_MY_DATA);

        keyBoardRows.add(keyBoardRow);

        keyBoardRow = new KeyboardRow();

        keyBoardRow.add(NamesOfMainMenuButtons.DELETE_MY_DATA);
        keyBoardRow.add(NamesOfMainMenuButtons.HELP);

        keyBoardRows.add(keyBoardRow);

        replyKeyboardMarkup.setKeyboard(keyBoardRows);
        return replyKeyboardMarkup;
    }

}
