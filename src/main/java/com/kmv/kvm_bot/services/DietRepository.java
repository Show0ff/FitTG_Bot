package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.entity.Diet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component

public interface DietRepository extends CrudRepository<Diet,Long> {
}
