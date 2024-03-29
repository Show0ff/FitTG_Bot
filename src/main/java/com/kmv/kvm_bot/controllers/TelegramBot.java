package com.kmv.kvm_bot.controllers;

import com.kmv.kvm_bot.config.BotConfig;

import com.kmv.kvm_bot.entity.Diet;
import com.kmv.kvm_bot.entity.Goal;
import com.kmv.kvm_bot.entity.User;
import com.kmv.kvm_bot.services.*;
import com.kmv.kvm_bot.utill.NamesOfMainMenuButtons;
import com.kmv.kvm_bot.utill.Strings;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private DietRepository dietRepository;
    @Autowired
    private UserRepository userRepository;
    private final BotConfig botConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private InLineKeyBoardService inLineKeyBoardService;
    @Autowired
    private MessageController messageController;
    @Autowired
    private CommandController commandController;
    private static final Logger log = LogManager.getLogger(TelegramBot.class);
    @Autowired
    private MainKeyBoardService mainKeyBoardService;
    @Autowired
    private FitCalcService fitCalcService;

    private static TelegramBot instance;

    public static TelegramBot getInstance() {
        return instance;
    }


    public TelegramBot(BotConfig config) {
        instance = this;
        this.botConfig = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "начать взаимодействие с ботом"));
        listOfCommands.add(new BotCommand("/deletedata", "удалить мои данные"));
        listOfCommands.add(new BotCommand("/help", "помощь по взаимодействию с ботом"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callBackUserParam(update);
        }
        String text = update.getMessage().getText();
        if (userRepository.findById(update.getMessage().getChatId()).isPresent() || text.equals("/start")) {
            if (!text.isEmpty()) {
                Message message = update.getMessage();
                long chatId = message.getChatId();
                messageController.addMessageInMessageList(message);
                log.info("message to bot " + message.getChat().getFirstName() + " " + text);
                sendMessageToAllUsers(text);
                callCommandPanel(update, text, chatId);
            }
            messageController.checkMessageAndSetParam(update);
        } else {
            sendMessage(update.getMessage().getChatId(), "Вы не зарегистрированы. Для регистрации введите /start");
        }
    }


    @SneakyThrows
    private void callBackUserParam(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = userRepository.findById(chatId).orElse(null);

        if (callBackData.equals(Strings.WEIGHT)) {
            sendMessage(chatId, "Введите Ваш вес: ");
            messageController.callBackList.add(callBackData);
        } else if (callBackData.equals(Strings.HEIGHT)) {
            sendMessage(chatId, "Введите Ваш рост: ");
            messageController.callBackList.add(callBackData);
        } else if (callBackData.equals(Strings.GOAL)) {
            messageController.callBackList.add(callBackData);
            execute(inLineKeyBoardService.setInlineButtonsForGoal(chatId));
        } else {
            try {
                Goal goal = Goal.valueOf(callBackData);
                user.setGoal(goal);
                userRepository.save(user);
                sendMessage(chatId, "Ваша цель установлена: " + goal);
            } catch (IllegalArgumentException e) {
                log.error("цель не была установлена" + e.getMessage());
            }
        }
    }


    private void callCommandPanel(Update update, String text, long chatId) throws TelegramApiException {
        switch (text) {
            case "/start" -> {
                if (userService.registerUser(update.getMessage())) {
                    sendMessage(chatId, commandController.startCommandReceived(update.getMessage().getChat().getFirstName()));
                } else {
                    sendMessage(chatId, commandController.startWithUserHowExist());
                }
            }
            case NamesOfMainMenuButtons.HELP -> sendMessage(chatId, Strings.HELP_TEXT);
            case NamesOfMainMenuButtons.SHOW_MY_DATA ->
                    sendMessage(chatId, Objects.requireNonNull(userService.getUserByChatId(chatId)).toString());
            case NamesOfMainMenuButtons.SET_MY_DATA ->
                    execute(inLineKeyBoardService.setInlineButtonsForCommandSetData(chatId));
            case NamesOfMainMenuButtons.DELETE_MY_DATA -> {
                userRepository.deleteById(chatId);
                sendMessage(chatId, "Ваши данные удалены, если хотите снова пользоваться ботом, то напишите /start");
            }
            case NamesOfMainMenuButtons.GET_ADVICE -> {
                User user = userRepository.findById(chatId).orElse(null);
                Diet diet;
                if (user.getGoal() == Goal.MAINTAIN_WEIGHT) {
                    diet = fitCalcService.calcForMaintain(user);
                    saveDietWithUserAndSendMessage(chatId, user, diet);
                } else if (user.getGoal() == Goal.LOSE_WEIGHT) {
                    diet = fitCalcService.calcForLose(user);
                    saveDietWithUserAndSendMessage(chatId, user, diet);
                } else if (user.getGoal() == Goal.GAIN_WEIGHT) {
                    diet = fitCalcService.calcForGain(user);
                    saveDietWithUserAndSendMessage(chatId, user, diet);
                } else {
                    sendMessage(chatId, "Заполните данные о себе. Нажмите кнопку " + NamesOfMainMenuButtons.SET_MY_DATA);
                }
            }

            default -> log.info("В чате " + chatId + text);
        }
    }

    private void saveDietWithUserAndSendMessage(long chatId, User user, Diet diet) {
        dietRepository.delete(diet);
        dietRepository.save(diet);
        user.getDiet().add(diet);
        userRepository.save(user);
        sendMessage(chatId, diet.toString());
    }

    public void sendMessageToAllUsers(String text) {
        String textToSend;
        if (text.contains("/send")) {
            textToSend = EmojiParser.parseToUnicode(text.substring(text.indexOf(" ")));
            Iterable<User> all = userRepository.findAll();
            for (User user : all) {
                sendMessage(user.getChatId(), textToSend);
            }
            log.info("message " + textToSend + " отправлен всем пользователям");
        }
    }

    @SneakyThrows
    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        sendMessage.setReplyMarkup(mainKeyBoardService.createReplyKeyboardMarkup());
        execute(sendMessage);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getToken();
    }

}
