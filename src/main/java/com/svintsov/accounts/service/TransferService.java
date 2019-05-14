package com.svintsov.accounts.service;

import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
import com.svintsov.accounts.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private AccountsRepository accountsRepository;

    public TransferService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void printAllAccounts() {
        List<Account> accountsList = new ArrayList<>();
        Iterable<Account> accountsIterable = accountsRepository.findAll();
        accountsIterable.forEach(accountsList::add);

        LOGGER.info("ALL ACCOUNTS ARE: {}", JsonUtils.silentToJsonString(accountsList));
    }

    public TransferResponse transfer(TransferRequest transferRequest) {
        return null;
    }
}
