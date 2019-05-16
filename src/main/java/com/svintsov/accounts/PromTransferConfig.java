package com.svintsov.accounts;

import com.svintsov.accounts.service.AccountsRepository;
import com.svintsov.accounts.service.PromTransferService;
import com.svintsov.accounts.service.TransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromTransferConfig {

    @Bean
    TransferService transferService(AccountsRepository accountsRepository) {
        return new PromTransferService(accountsRepository);
    }

}
