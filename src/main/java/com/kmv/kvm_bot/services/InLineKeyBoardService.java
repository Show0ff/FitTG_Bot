package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.entity.Goal;
import com.kmv.kvm_bot.utill.Strings;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class InLineKeyBoardService {

    @SneakyThrows
    public SendMessage setInlineButtonsForCommandSetData(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Что ты хочешь изменить?");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton weightButton = new InlineKeyboardButton();
        weightButton.setText("Вес");
        weightButton.setCallbackData(Strings.WEIGHT);

        InlineKeyboardButton heightButton = new InlineKeyboardButton();
        heightButton.setText("Рост");
        heightButton.setCallbackData(Strings.HEIGHT);

        InlineKeyboardButton goalWeightData = new InlineKeyboardButton();
        goalWeightData.setText("Цель");
        goalWeightData.setCallbackData(Strings.GOAL);

        rowInLine.add(weightButton);
        rowInLine.add(heightButton);
        rowInLine.add(goalWeightData);

        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    @SneakyThrows
    public SendMessage setInlineButtonsForGoal(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выбери свою цель: ");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton weightButton = new InlineKeyboardButton();
        weightButton.setText(Goal.GAIN_WEIGHT.toString());
        weightButton.setCallbackData(Goal.GAIN_WEIGHT.toString());

        InlineKeyboardButton heightButton = new InlineKeyboardButton();
        heightButton.setText(Goal.LOSE_WEIGHT.toString());
        heightButton.setCallbackData(Goal.LOSE_WEIGHT.toString());

        InlineKeyboardButton goalWeightData = new InlineKeyboardButton();
        goalWeightData.setText(Goal.MAINTAIN_WEIGHT.toString());
        goalWeightData.setCallbackData(Goal.MAINTAIN_WEIGHT.toString());

        rowInLine.add(weightButton);
        rowInLine.add(heightButton);
        rowInLine.add(goalWeightData);

        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }


}
