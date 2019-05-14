package com.svintsov.accounts.rest;

import com.svintsov.accounts.App;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AccountsControllerTest.Initializer.class)
public class AccountsControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsControllerTest.class);

    @Autowired
    AccountsController accountsController;

    @Test
    public void shouldTransfer() {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFrom("1");
        transferRequest.setTo("2");
        transferRequest.setAmount(250.);
        TransferResponse transferResponse = accountsController.transfer(transferRequest);
        Assert.assertEquals(transferResponse.getSuccess(), true);
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            LOGGER.info("Initializing test context...");
        }
    }

}
