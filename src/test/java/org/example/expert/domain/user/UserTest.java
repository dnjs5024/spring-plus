package org.example.expert.domain.user;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class UserTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    void setUp(){
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 1_000_000; i++) {
            User user = new User(
                "user" + i + "@example.com",
                "password" + i,
                UserRole.USER,
                "User" + i
            );
            users.add(user);

            // 배치 단위로 flush & clear
            if (i % 1000 == 0) {
                userRepository.saveAll(users);
                users.clear();
                em.flush();
                em.clear();
            }
        }

        // 남은 유저 저장
        if (!users.isEmpty()) {
            userRepository.saveAll(users);
            em.flush();
            em.clear();
        }

        System.out.println("유저 100만명 저장 완료");
    }


}
