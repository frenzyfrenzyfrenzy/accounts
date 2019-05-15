package com.svintsov.accounts.rest;

import com.svintsov.accounts.model.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestUtils {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private static final List<String> NAMES = Arrays.asList("Peter", "Mike", "Stan", "Bob", "Mark");

    public static Account createRandomAccount(Integer maxMoney) {
        return new Account(null, NAMES.get(RANDOM.nextInt(NAMES.size())), (double) RANDOM.nextInt(maxMoney));
    }

}
