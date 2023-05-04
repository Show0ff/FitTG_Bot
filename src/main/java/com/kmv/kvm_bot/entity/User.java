package com.kmv.kvm_bot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {

    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;

    private int weight;
    private int height;
    private Goal goal = Goal.WITH_OUT_GOAL;
    private Timestamp joinTime;

    @Override
    public String toString() {
        return
                "  Имя: " + firstName +
                        "\n Рост: " + height +
                        "\n Вес: " + weight +
                        "\n Цель: " + goal.toString() +
                        "\n Дата присоединения " + joinTime.toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
