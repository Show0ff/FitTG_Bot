package com.kmv.kvm_bot.controllers;

import com.kmv.kvm_bot.entity.User;
import com.kmv.kvm_bot.services.TelegramBot;
import com.kmv.kvm_bot.services.UserRepository;
import com.kmv.kvm_bot.utill.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class MessageController {
    @Autowired
    private UserRepository userRepository;
    private final HashMap<Long, List<Message>> messageHashMap = new HashMap<>();
    public final List<String> callBackList = new ArrayList<>();




    public void addMessageInMessageList(Message message) {
        if (messageHashMap.containsKey(message.getChatId())) {
            messageHashMap.get(message.getChatId()).add(message);
        } else {
            messageHashMap.put(message.getChatId(), new ArrayList<>());
            messageHashMap.get(message.getChatId()).add(message);
        }
    }

    public void checkMessageAndSetParam(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Message> messages = messageHashMap.get(chatId);
        if (messages.get(messages.size() - 2).getText().equals("/setmydata")) {
            User user = userRepository.findById(chatId).orElse(null);
            String lastValueOfCallBackList = callBackList.get(callBackList.size() - 1);
            String text = update.getMessage().getText();
            if (lastValueOfCallBackList.equals(Strings.WEIGHT)) {
                user.setWeight(Integer.parseInt(text));
                TelegramBot.getInstance().sendMessage(chatId, "Ваш вес установлен: " + text + " кг");
            } else if (lastValueOfCallBackList.equals(Strings.HEIGHT)) {
                user.setHeight(Integer.parseInt(text));
                TelegramBot.getInstance().sendMessage(chatId, "Ваш рост установлен: " + text + " см");
            }
            userRepository.save(user);
        }
    }


}
