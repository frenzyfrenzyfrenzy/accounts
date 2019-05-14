package com.svintsov.accounts.rest;

import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountsApi {

    @POST("/transfer")
    Call<TransferResponse> transfer(@Body TransferRequest transferRequest);

}
