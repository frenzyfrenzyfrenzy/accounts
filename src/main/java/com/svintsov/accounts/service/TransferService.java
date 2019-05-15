package com.svintsov.accounts.service;

import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.utils.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private AccountsRepository accountsRepository;

    public TransferService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferMoney(Integer from, Integer to, Double amount) {
        Account fromAccount = accountsRepository.findById(from).orElseThrow(() -> new DatabaseException(String.format("Error getting account by id %s", from)));
        Account toAccount = accountsRepository.findById(to).orElseThrow(() -> new DatabaseException(String.format("Error getting account by id %s", from)));
        if (fromAccount.getSum() - amount < 0) throw new DatabaseException("Insufficient funds on sender account");

        fromAccount.setSum(fromAccount.getSum() - amount);
        toAccount.setSum(toAccount.getSum() + amount);
        accountsRepository.save(fromAccount);
        accountsRepository.save(toAccount);
    }

}
