package com.test.repository;

import com.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findUserById(long id);

    @Query(value = "UPDATE user SET credit_card = :credit WHERE id =:id", nativeQuery = true)
    @Modifying(clearAutomatically = true)
    @Transactional
    void updateCreditCard(@Param("id") long id, @Param("credit") String creditCard);
}