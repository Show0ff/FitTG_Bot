package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(UserService.class);

    public boolean registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            Chat chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setJoinTime(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user was saved " + user);
            return true;
        } else {
            return false;
        }
    }

    public User getUserByChatId(long chatId) {
        if (userRepository.findById(chatId).isPresent()) {
            return userRepository.findById(chatId).get();
        } else {
            log.error("user by " + chatId + " не был найден");
            return null;
        }
    }


}
