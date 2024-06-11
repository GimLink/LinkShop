package com.linksang.LinkShop.service;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@Service
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Value("${coolsms.apiKey}")
    String apiKey;

    @Value("${coolsms.apiSecret}")
    String apiSecret;

    @Value("${coolsms.fromNumber}")
    String fromNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendMessage(String toNumber, int randomNumber) {

        Message coolsms = new Message();

        coolsms.setTo(toNumber);
        coolsms.setFrom(fromNumber);
        coolsms.setType(MessageType.SMS);
        coolsms.setText("LinkShop 인증번호 " + randomNumber + "를 입력하세요.");


        try{
            MultipleDetailMessageSentResponse response = this.messageService.send(coolsms);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException |
                 NurigoUnknownException e) {
            log.info("MessageService 50 line : send failed and " + e.getMessage());
        }

    }

    public int randomNum() {
        int maxNum = 999999;
        int minNum = 100000;

        return ThreadLocalRandom.current().nextInt(minNum, maxNum + 1);
    }

    public boolean phoneValidationCheck(String phoneNum){
        String phonePattern = "^(010[1-9][0-9]{7})$";
        return Pattern.matches(phonePattern, phoneNum);
    }

}
