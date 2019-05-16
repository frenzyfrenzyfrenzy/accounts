package com.svintsov.accounts.service;

import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.utils.DatabaseException;
import com.svintsov.accounts.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class PromTransferService implements TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromTransferService.class);

    private AccountsRepository accountsRepository;

    public PromTransferService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferMoney(Integer from, Integer to, Integer amount) {
        LOGGER.info("Trying to transfer {} from {} to {}", amount, from, to);

        if (from.equals(to)) throw new DatabaseException("Sending to itself is prohibited");
        Account fromAccount = accountsRepository.findById(from).orElseThrow(() -> new DatabaseException(String.format("Error getting account by id %d", from)));
        Account toAccount = accountsRepository.findById(to).orElseThrow(() -> new DatabaseException(String.format("Error getting account by id %d", from)));
        if (fromAccount.getSum() - amount < 0) throw new DatabaseException("Insufficient funds on sender account");

        String fromAccountBeforeString = JsonUtils.silentToJsonString(fromAccount);
        String toAccountBeforeString = JsonUtils.silentToJsonString(toAccount);
        fromAccount.setSum(fromAccount.getSum() - amount);
        toAccount.setSum(toAccount.getSum() + amount);
        String fromAccountAfterString = JsonUtils.silentToJsonString(fromAccount);
        String toAccountAfterString = JsonUtils.silentToJsonString(toAccount);
        accountsRepository.save(fromAccount);
        accountsRepository.save(toAccount);
        LOGGER.info("Transfer finished. \n Amount: {} \n Before:\n {} \n {} \n After:\n {} \n {}", amount, fromAccountBeforeString, toAccountBeforeString, fromAccountAfterString, toAccountAfterString);
    }

}
