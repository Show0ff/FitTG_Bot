package com.kmv.kvm_bot.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
@Transactional
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Diet> diet = new ArrayList<>();

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
