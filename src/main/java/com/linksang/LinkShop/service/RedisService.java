package com.linksang.LinkShop.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);

    public void setAuthNo(String phoneNum, int authNum) throws Exception {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Duration time = Duration.ofMinutes(3);

        String check = phoneNum + "_check";

        try {
            vop.set(phoneNum, authNum, time);
            vop.set(check, false, time);
        } catch (Exception e) {
            log.info("RedisService exception : setAuthNo method error : " + e.getMessage());
            throw new Exception("인증번호 저장 에러");
        }
    }

    public int getAuthNum(String phoneNum) {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Object result = vop.get(phoneNum);

        if (!(result instanceof Integer)) {
            return 1;
        } else {
            return (int) result;
        }
    }

    public void deleteKey(String key) {

        redisTemplate.delete(key);
    }

    public void setAuthCheck(String phoneNum) throws Exception {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Duration time = Duration.ofHours(12);
        String check = phoneNum + "_check";

        try {
            vop.set(check, true, time);
        } catch (Exception e) {
            log.error("redisService, setAuthCheck 메서드 에러");
            throw new Exception("redisService, setAuthCheck 메서드 에러");
        }
    }

    public boolean confirmPhoneCheck(String phoneNum) {
        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        Object result = vop.get(phoneNum + "_check");

        if (result instanceof Boolean) {
            return (boolean) result;
        } else {
            return false;
        }
    }

    public int authNumCheck(String phoneNum, String dtoAuthNum) {

        ValueOperations<String, Object> vop = redisTemplate.opsForValue();

        Object realAuthNum = vop.get(phoneNum);
        int authNum = Integer.parseInt(dtoAuthNum); //사용자가 입력한 인증번호

        if (realAuthNum != null) {
            int authResult = (int) realAuthNum;
            if (authResult == authNum) {
                return 1;
            } else {
                return 2;
            }
        }

        return 3;
    }

}
