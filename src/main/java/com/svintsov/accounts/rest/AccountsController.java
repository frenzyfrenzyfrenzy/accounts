package com.svintsov.accounts.rest;

import com.google.common.collect.ImmutableList;
import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.model.TransferResponse;
import com.svintsov.accounts.service.AccountsRepository;
import com.svintsov.accounts.service.TransferService;
import com.svintsov.accounts.utils.JsonUtils;
import com.svintsov.accounts.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountsController {

    @Autowired
    Validator validator;

    @Autowired
    TransferService transferService;

    @Autowired
    AccountsRepository accountsRepository;

    @ResponseBody
    @RequestMapping(path = "/transferMoney", method = RequestMethod.POST)
    public TransferResponse transfer(@RequestBody TransferRequest transferRequest) {
        TransferResponse transferResponse = new TransferResponse();
        List<String> validationErrors = validator.validateTransferRequest(transferRequest);
        if (validationErrors != null && !validationErrors.isEmpty()) {
            transferResponse.setSuccess(false);
            transferResponse.setErrorText(JsonUtils.silentToJsonString(validationErrors));
            return transferResponse;
        } else {
            transferService.printAllAccounts();
            transferResponse.setSuccess(true);
            return transferResponse;
        }
    }

    @ResponseBody
    @RequestMapping(path = "/getAllAccounts", method = RequestMethod.GET)
    public List<Account> getAll() {
        return ImmutableList.copyOf(accountsRepository.findAll());
    }

    @ResponseBody
    @RequestMapping(path = "/addAccount", method = RequestMethod.POST)
    public Account add(@RequestBody Account account) {
        return accountsRepository.save(account);
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
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public TransferResponse handleRuntimeException(RuntimeException exception) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setSuccess(false);
        transferResponse.setErrorText(exception.getMessage());
        return transferResponse;
    }

}
