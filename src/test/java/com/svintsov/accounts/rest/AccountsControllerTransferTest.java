package com.svintsov.accounts.rest;

import com.svintsov.accounts.App;
import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.GeneralResponse;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class AccountsControllerTransferTest extends AccountsControllerBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsControllerTransferTest.class);

    @LocalServerPort
    String localServerPort;

    @Test
    @SuppressWarnings("Duplicates")
    public void shouldTransferMoneyConsistently() throws IOException {
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

        Assert.assertEquals(sumBefore.get(), sumAfter.get());
    }

    @Test
    public void shouldTransferMoney() throws IOException {
        Integer maxMoney = 1000;

        Account firstAccountBefore = accountsApiTestEndpoint.addAccount(TestUtils.createRandomAccount(maxMoney)).execute().body().getBody();
        Account secondAccountBefore = accountsApiTestEndpoint.addAccount(TestUtils.createRandomAccount(maxMoney)).execute().body().getBody();

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFrom(firstAccountBefore.getId());
        transferRequest.setTo(secondAccountBefore.getId());
        Integer amountToTransfer = RANDOM.nextInt(firstAccountBefore.getSum());
        transferRequest.setAmount(amountToTransfer);
        accountsApiTestEndpoint.transferMoney(transferRequest).execute();

        List<Account> accountsAfter = accountsApiTestEndpoint.getAllAccounts().execute().body().getBody();

        Account firstAccountAfter = accountsAfter.stream().filter(account -> account.getId().equals(firstAccountBefore.getId())).findAny().get();
        Account secondAccountAfter = accountsAfter.stream().filter(account -> account.getId().equals(secondAccountBefore.getId())).findAny().get();

        Assert.assertTrue(firstAccountAfter.getSum() == firstAccountBefore.getSum() - amountToTransfer);
        Assert.assertTrue(secondAccountAfter.getSum() == secondAccountBefore.getSum() + amountToTransfer);
    }

    @Test
    public void shouldFailOnValidation() throws IOException {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFrom(1);
        transferRequest.setTo(2);
        Response<GeneralResponse> transferResponse = accountsApiTestEndpoint.transferMoney(transferRequest).execute();
        Assert.assertFalse(transferResponse.body().isSuccess());
        Assert.assertTrue(!transferResponse.body().getError().isEmpty());
    }

}
