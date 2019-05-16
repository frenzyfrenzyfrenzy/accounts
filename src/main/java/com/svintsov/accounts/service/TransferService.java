package com.svintsov.accounts.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public interface TransferService {
    @Transactional(isolation = Isolation.READ_COMMITTED)
    void transferMoney(Integer from, Integer to, Integer amount);
}
