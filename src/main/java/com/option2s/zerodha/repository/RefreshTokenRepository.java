package com.option2s.zerodha.repository;

import com.option2s.zerodha.model.RefreshToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @NotNull
    @Query("SELECT r FROM RefreshToken r ORDER BY r.date DESC")
    List<RefreshToken> findAll();

    @Query("SELECT r FROM RefreshToken r WHERE r.date=?1 ORDER BY r.date DESC")
    List<RefreshToken> findAllByDate(Date date);

}
