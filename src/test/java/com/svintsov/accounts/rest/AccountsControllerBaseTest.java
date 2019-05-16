package com.svintsov.accounts.rest;

import com.svintsov.accounts.service.AccountsRepository;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Random;

public class AccountsControllerBaseTest {

    @LocalServerPort
    String localServerPort;

    protected AccountsApi accountsApiTestEndpoint;

    @Autowired
    protected AccountsRepository accountsRepository;

    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    @Before
    public void beforeTest() {
        accountsRepository.deleteAll();

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
        accountsApiTestEndpoint = retrofit.create(AccountsApi.class);
    }

}
