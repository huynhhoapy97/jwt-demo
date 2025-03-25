package com.huynhhoapy97.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huynhhoapy97.models.Account;
import com.huynhhoapy97.models.AccountToken;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class AccountService {
    private final String jwtSecretKey = "f8b79836f1a388c618dc4e6f2252ed6b45259e1aff98c440790a996762591c8a23638e9844a8c049055ed62e193cc00121e70c3ffe2a8717a617420ddcf000ae5322a105131cdc36e195fde61c3e965f6271c103c7db5b549e29cf492448fbbefa02020f01b8a25f2415ffca5542f8bf33bde549cbdd22a7ab71ba5243aabdcad2df386fb513cb7e1bb4cfa80e20f54264e4c42ea982cbdf7a58c4c79982e84c899ddb4f79495e4e5d2b946672d62fe61f90830ec6b7fb3347e9a6982be0712454edc50f7d071b2997970f264bf7135eb43e72fe23962127e885288712c3f6d5de790d0dfb079d1bfcac24f875736eddb31393cf35a32a8eca620c4e63dc7dc1";

    public String createJWT(Account account) {
        StringBuilder token = new StringBuilder();
        ObjectMapper json = new ObjectMapper();
        AccountToken accountToken = new AccountToken(
                account.getUserName(),
                getExpiredTime());

        try {
            String header = "{\"sub\": \"\",\"typ\": \"JWT\"}";
            String payload = json.writeValueAsString(accountToken);

            String encodedHeader = Base64.getUrlEncoder()
                    .encodeToString(header.getBytes())
                    .replace("=", "");
            String encodedPayload = Base64.getUrlEncoder()
                    .encodeToString(payload.getBytes())
                    .replace("=", "");
            String tokenData = String.join(".", encodedHeader, encodedPayload);

            String signature = getSignature(tokenData);

            token.append(tokenData);
            token.append(".");
            token.append(signature);
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return token.toString();
    }

    public String getSignature(String tokenData) throws NoSuchAlgorithmException, InvalidKeyException {
        // Sử dụng thuật toán băm HMAC-SHA256 để băm tokenData và jwtSecretKey
        String algorithm = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecretKey.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(tokenData.getBytes());

        return Base64.getUrlEncoder()
                .encodeToString(hmacBytes)
                .replace("=", "");
    }

    private String getExpiredTime() {
        Instant instant = Instant.now().plus(30, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

        return formatter.format(instant);
    }
}
