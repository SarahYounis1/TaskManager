package com.example.taskmanager.daoRepository;
import com.example.taskmanager.Entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface TokenRepository extends JpaRepository<Tokens,String> {
    @Transactional
    void deleteAllByUserId(Long userId);
}
