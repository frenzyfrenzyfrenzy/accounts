package com.svintsov.accounts.rest;

import com.google.common.collect.ImmutableList;
import com.svintsov.accounts.model.Account;
import com.svintsov.accounts.model.GeneralResponse;
import com.svintsov.accounts.model.TransferRequest;
import com.svintsov.accounts.service.AccountsRepository;
import com.svintsov.accounts.service.TransferService;
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

    @Autowired
    TransferService transferService;

    @Autowired
    AccountsRepository accountsRepository;

    @ResponseBody
    @RequestMapping(path = "/transferMoney", method = RequestMethod.POST)
    public GeneralResponse transfer(@RequestBody TransferRequest transferRequest) {
        List<String> validationErrors = validator.validateTransferRequest(transferRequest);
        if (validationErrors != null && !validationErrors.isEmpty())
            return GeneralResponse.error(JsonUtils.silentToJsonString(validationErrors));
        else {
            transferService.transferMoney(transferRequest.getFrom(), transferRequest.getTo(), transferRequest.getAmount());
            return GeneralResponse.success(null);
        }
    }

    @ResponseBody
    @RequestMapping(path = "/getAllAccounts", method = RequestMethod.GET)
    public GeneralResponse<List<Account>> getAll() {
        return GeneralResponse.success(ImmutableList.copyOf(accountsRepository.findAll()));
    }

    @ResponseBody
    @RequestMapping(path = "/addAccount", method = RequestMethod.POST)
    public GeneralResponse<Account> add(@RequestBody Account account) {
        return GeneralResponse.success(accountsRepository.save(account));
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponse handleBadRequest(HttpMessageNotReadableException exception) {
        return GeneralResponse.error(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralResponse handleRuntimeException(RuntimeException exception) {
        return GeneralResponse.error(exception.getMessage());
    }

}
