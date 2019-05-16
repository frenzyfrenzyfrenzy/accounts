package com.svintsov.accounts.test;

import com.svintsov.accounts.service.AccountsRepository;
import com.svintsov.accounts.service.TransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestTransferConfig {

    @Bean
    TransferService transferService(AccountsRepository accountsRepository) {
        return new TestTransferService(accountsRepository);
    }

}
