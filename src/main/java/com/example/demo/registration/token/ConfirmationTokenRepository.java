package com.example.demo.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.comfirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
