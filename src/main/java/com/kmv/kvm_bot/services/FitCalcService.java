package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.entity.Diet;
import com.kmv.kvm_bot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FitCalcService {
    @Autowired
    private UserRepository userRepository;


    public Diet calcForMaintain(User user) {
        int weightOfUser = user.getWeight();

        int normOfCalorie = weightOfUser * 32;

        return getDiet(user, weightOfUser, normOfCalorie);

    }

    private Diet getDiet(User user, int weightOfUser, int normOfCalorie) {
        int normOfProtein = (int) (weightOfUser * 2.5);
        int normOfCarbohydrates = (normOfCalorie - normOfProtein * 4 - weightOfUser * 9) / 4;

        return Diet.builder()
                .user(userRepository.findById(user.getChatId()).orElse(null))
                .calories(normOfCalorie)
                .protein(normOfProtein)
                .fats(weightOfUser)
                .carbohydrates(normOfCarbohydrates)
                .build();
    }

    public Diet calcForGain(User user) {
        int weightOfUser = user.getWeight();

        int normOfCalorie = (int) ((weightOfUser * 32) * 1.15);

        return getDiet(user, weightOfUser, normOfCalorie);

    }

    public Diet calcForLose(User user) {
        int weightOfUser = user.getWeight();

        int normOfCalorie = (int) ((weightOfUser * 32) * 0.8);

        return getDiet(user, weightOfUser, normOfCalorie);

    }

}


// 1 углевод = 4 калории
// 1 белок = 4 калории
// 1 жир = 9 калорий
// калории = вес * 32
// вода = 40 мл * вес

// Пропорции для сушки
// 1г жира = 1 кг веса
// 1г белка = 3 кг веса
// Остаток в углеводы

// расчёт для поддержки
// Для поддержки веса нужно есть
// калории: вес * 32
// жиры 1:1
// белки 1:2.5
// углеводы в остатке
