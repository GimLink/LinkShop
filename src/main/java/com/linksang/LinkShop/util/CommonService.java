package com.linksang.LinkShop.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CommonService {

    public String getErrorMessage(BindingResult errors) {

        String message = "";
        for (FieldError error : errors.getFieldErrors()) {
            message = error.getDefaultMessage();
            break;
        }
        return message;
    }

    public int randomNum() {
        int maxNum = 999999999;
        int minNum = 100000000;

        return ThreadLocalRandom.current().nextInt(minNum, maxNum + 1);
    }

    public int randomAuthNum() {

        int maxNum = 999999;
        int minNum = 100000;

        return ThreadLocalRandom.current().nextInt(minNum, maxNum + 1);
    }
}
