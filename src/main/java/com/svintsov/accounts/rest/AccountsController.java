package com.svintsov.accounts.rest;

import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
import com.svintsov.accounts.utils.JsonUtils;
import com.svintsov.accounts.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AccountsController {

    @Autowired
    Validator validator;

    @ResponseBody
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public TransferResponse translate(@RequestBody TransferRequest transferRequest) {
        TransferResponse transferResponse = new TransferResponse();
        List<String> validationErrors = validator.validateTransferRequest(transferRequest);
        if (validationErrors != null && !validationErrors.isEmpty()) {
            transferResponse.setSuccess(false);
            transferResponse.setErrorText(JsonUtils.silentToJsonString(validationErrors));
            return transferResponse;
        }
        else {
            transferResponse.setSuccess(true);
            return transferResponse;
        }
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TransferResponse handleBadRequest(HttpMessageNotReadableException exception) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setSuccess(false);
        transferResponse.setErrorText(exception.getMessage());
        return transferResponse;
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public TransferResponse handleRuntimeException(RuntimeException exception) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setSuccess(false);
        transferResponse.setErrorText(exception.getMessage());
        return transferResponse;
    }

}
