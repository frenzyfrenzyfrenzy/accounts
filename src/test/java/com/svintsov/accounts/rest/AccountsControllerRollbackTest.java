package com.svintsov.accounts.rest;

import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.test.TestApp;
import com.svintsov.accounts.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class AccountsControllerRollbackTest extends AccountsControllerBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsControllerRollbackTest.class);

    @Test
    @SuppressWarnings("Duplicates")
    public void shouldRollback() throws IOException {
        Integer maxMoney = 1000;
        Integer transferRequestsCount = 10;
        Integer accountsCount = 5;

        AtomicInteger sumBefore = new AtomicInteger(0);
        AtomicInteger sumAfter = new AtomicInteger(0);

        for (int i = 0; i < accountsCount; ++i)
            accountsApiTestEndpoint.addAccount(TestUtils.createRandomAccount(maxMoney)).execute();

        final List<Account> accountsBefore = accountsApiTestEndpoint.getAllAccounts().execute().body().getBody();
        accountsBefore.forEach(it -> sumBefore.set(sumBefore.intValue() + it.getSum()));
        LOGGER.info("ACCOUNTS BEFORE: {}", JsonUtils.silentToJsonString(accountsBefore));
        LOGGER.info("SUM BEFORE: {}", sumBefore.get());

        for (int i = 0; i < transferRequestsCount; ++i) {
            TransferRequest transferRequest = new TransferRequest();
            transferRequest.setFrom(accountsBefore.get(RANDOM.nextInt(accountsBefore.size())).getId());
            transferRequest.setTo(accountsBefore.get(RANDOM.nextInt(accountsBefore.size())).getId());
            transferRequest.setAmount(RANDOM.nextInt(maxMoney));
            accountsApiTestEndpoint.transferMoney(transferRequest).execute();
        }

        final List<Account> accountsAfter = accountsApiTestEndpoint.getAllAccounts().execute().body().getBody();
        accountsAfter.forEach(it -> sumAfter.set(sumAfter.intValue() + it.getSum()));
        LOGGER.info("ACCOUNTS AFTER: {}", JsonUtils.silentToJsonString(accountsAfter));
        LOGGER.info("SUM AFTER: {}", sumAfter.get());

        Assert.assertEquals(JsonUtils.silentToJsonString(accountsBefore),JsonUtils.silentToJsonString(accountsAfter));
    }


}
