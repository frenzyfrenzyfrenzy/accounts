package com.svintsov.accounts.rest;

import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.GeneralResponse;
import com.svintsov.accounts.model.TransferRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface AccountsApi {

    @POST("/transferMoney")
    Call<GeneralResponse> transferMoney(@Body TransferRequest transferRequest);

    @GET("/getAllAccounts")
    Call<GeneralResponse<List<Account>>> getAllAccounts();

    @POST("/addAccount")
    Call<GeneralResponse<Account>> addAccount(@Body Account account);

}
