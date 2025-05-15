package org.example.expert.domain.user.repository;

import java.util.List;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByUserName(String userName);

    @Query("select u from User u where u.userName = :userName")
    List<User> findByUerNameJPQL(@Param("userName") String userName);
}
