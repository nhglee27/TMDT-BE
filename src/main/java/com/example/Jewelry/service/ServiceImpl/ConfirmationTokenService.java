package com.example.Jewelry.service.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.Jewelry.dao.ConfirmationTokenDAO;
import com.example.Jewelry.entity.ConfirmationToken;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenDAO dao;

    public Optional<ConfirmationToken> getToken(String token) {
        return dao.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return dao.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public void save(ConfirmationToken confirmationToken) {
        dao.save(confirmationToken);
    }
}

