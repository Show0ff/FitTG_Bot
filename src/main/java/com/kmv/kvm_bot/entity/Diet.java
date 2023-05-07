package com.kmv.kvm_bot.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "diet")
@Transactional
public class Diet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private User user;
    private Integer fats;
    private Integer carbohydrates;
    private Integer protein;
    private Integer calories;

    @Override
    public String toString() {
        return "Диета" +
                ", Жиры = " + fats +
                ", Углеводы = " + carbohydrates +
                ", Белки = " + protein +
                ", Общее количество калорий = " + calories +
                '}';
    }
}
