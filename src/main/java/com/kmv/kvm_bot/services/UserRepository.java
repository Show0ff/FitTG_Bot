package com.kmv.kvm_bot.services;

import com.kmv.kvm_bot.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
