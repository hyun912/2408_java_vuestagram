package com.example.vuestagram.repogitory;


import com.example.vuestagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepogitory extends JpaRepository<User, Long> {
	Optional<User> findByAccount(String account);
}
