package com.svintsov.accounts.rest;

import com.svintsov.accounts.App;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

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

    @Before
    public void beforeTest() {
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
    public void shouldTransfer() throws IOException {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFrom("1");
        transferRequest.setTo("2");
        transferRequest.setAmount(250.);
        Response<TransferResponse> transferResponse = accountsApi.transfer(transferRequest).execute();
        Assert.assertEquals(transferResponse.body().getSuccess(), true);
    }

    @Test
    public void shouldFailOnValidation() throws IOException {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFrom("1");
        transferRequest.setTo("2");
        Response<TransferResponse> transferResponse = accountsApi.transfer(transferRequest).execute();
        Assert.assertEquals(transferResponse.body().getSuccess(), false);
        Assert.assertTrue(!transferResponse.body().getErrorText().isEmpty());
    }

}
