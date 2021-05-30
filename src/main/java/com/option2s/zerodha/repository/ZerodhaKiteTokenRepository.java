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

    @Query(nativeQuery = true, value = "SELECT * FROM zerodha_kite_tokens WHERE " +
            "request_token_value IS NOT NULL AND CAST(date AS DATE)=DATE(timezone('UTC', CURRENT_DATE)) " +
            "ORDER BY date DESC LIMIT 1")
    ZerodhaKiteToken findLatestRequestToken();


    @Query(nativeQuery = true, value = "SELECT * FROM zerodha_kite_tokens WHERE " +
            "CAST(date AS DATE)=DATE(timezone('UTC', CURRENT_DATE)) ORDER BY date DESC;")
    List<ZerodhaKiteToken> findTokensByCurrentDate();

}
