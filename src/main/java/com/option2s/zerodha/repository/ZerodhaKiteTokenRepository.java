package com.option2s.zerodha.repository;

import com.option2s.zerodha.model.ZerodhaKiteToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ZerodhaKiteTokenRepository extends JpaRepository<ZerodhaKiteToken, Long> {

    @NotNull
    @Query("SELECT r FROM ZerodhaKiteToken r ORDER BY r.date DESC")
    List<ZerodhaKiteToken> findAll();

    @Query("SELECT r FROM ZerodhaKiteToken r WHERE r.date=?1 ORDER BY r.date DESC")
    List<ZerodhaKiteToken> findAllByDate(Date date);

    @Query(nativeQuery = true, value = "SELECT * FROM zerodha_kite_tokens WHERE " +
            "request_token_value IS NOT NULL ORDER BY date DESC LIMIT 1")
    ZerodhaKiteToken findLatest();

    ZerodhaKiteToken findFirstByOrderByDateDesc();

}
