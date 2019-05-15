package com.svintsov.accounts.service;

import com.svintsov.accounts.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountsRepository extends CrudRepository<Account, Integer> {
}
