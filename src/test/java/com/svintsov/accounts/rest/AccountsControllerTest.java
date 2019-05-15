package com.svintsov.accounts.rest;

import com.google.common.util.concurrent.AtomicDouble;
import com.svintsov.accounts.App;
import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {App.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class AccountsControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsControllerTest.class);

    @LocalServerPort
    String localServerPort;

    private AccountsApi accountsApi;
    private ExecutorService executorService;
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Before
    public void beforeTest() {
        executorService = Executors.newFixedThreadPool(4);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(String.format("http://localhost:%s/", localServerPort))
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        accountsApi = retrofit.create(AccountsApi.class);
    }

    @Test
    public void shouldTransferMoney() throws IOException, InterruptedException {
        AtomicDouble sumBefore = new AtomicDouble(0.);
        AtomicDouble sumAfter = new AtomicDouble(0.);

        accountsApi.addAccount(TestUtils.createRandomAccount(1000)).execute();
        accountsApi.addAccount(TestUtils.createRandomAccount(1000)).execute();
        accountsApi.addAccount(TestUtils.createRandomAccount(1000)).execute();
        accountsApi.addAccount(TestUtils.createRandomAccount(1000)).execute();
        accountsApi.addAccount(TestUtils.createRandomAccount(1000)).execute();

        final List<Account> accountsBefore = accountsApi.getAllAccounts().execute().body().getBody();
        accountsBefore.forEach(it -> sumBefore.set(sumBefore.doubleValue() + it.getSum()));
        LOGGER.info("ACCOUNTS BEFORE: {}", JsonUtils.silentToJsonString(accountsBefore));
        LOGGER.info("SUM BEFORE: {}", sumBefore.get());

        for (int i = 0; i < 10; ++i) {
//            executorService.submit(() -> {
                TransferRequest transferRequest = new TransferRequest();
                transferRequest.setFrom(accountsBefore.get(RANDOM.nextInt(accountsBefore.size())).getId());
                transferRequest.setTo(accountsBefore.get(RANDOM.nextInt(accountsBefore.size())).getId());
                transferRequest.setAmount(RANDOM.nextDouble() * 1000);
                try {
                    accountsApi.transferMoney(transferRequest).execute();
                } catch (IOException e) {
                    LOGGER.error("FAILED TO TRANSFER", e);
                }
//            });
        }

//        executorService.awaitTermination(20, TimeUnit.SECONDS);

        final List<Account> accountsAfter = accountsApi.getAllAccounts().execute().body().getBody();
        accountsAfter.forEach(it -> sumAfter.set(sumAfter.doubleValue() + it.getSum()));
        LOGGER.info("ACCOUNTS AFTER: {}", JsonUtils.silentToJsonString(accountsAfter));
        LOGGER.info("SUM AFTER: {}", sumAfter.get());

        Assert.assertEquals(sumBefore.get(), sumAfter.get(), 1.);
    }

//    @Test
//    public void shouldFailOnValidation() throws IOException {
//        TransferRequest transferRequest = new TransferRequest();
//        transferRequest.setFrom(1);
//        transferRequest.setTo(2);
//        Response<GeneralResponse> transferResponse = accountsApi.transferMoney(transferRequest).execute();
//        Assert.assertEquals(transferResponse.body().getSuccess(), false);
//        Assert.assertTrue(!transferResponse.body().getErrorText().isEmpty());
//    }

}
