package com.huynhhoapy97.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huynhhoapy97.models.Account;
import com.huynhhoapy97.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("home-page")
    public String homePage(ModelMap modelMap) {
        modelMap.addAttribute("account", new Account());

        return "home-page";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("account") Account account,
                        ModelMap modelMap,
                        HttpServletResponse response) {
        // Tạo JWT token
        String token = accountService.createJWT(account);

        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setHttpOnly(true);

        response.addCookie(cookie);
        modelMap.addAttribute("account", account);

        return "dashboard";
    }

    @ResponseBody
    @PostMapping("access-data")
    public ResponseEntity<String> accessData(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody String jsonData) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        if (!authHeader.contains("Bearer") ||
                authHeader.trim().length() == 6) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

        System.out.println(authHeader.substring(7));
        // eyJzdWIiOiAiIiwidHlwIjogIkpXVCJ9.
        // eyJzdWIiOiJob2EiLCJleHAiOiIyMDI1LTA0LTIzIDIzOjU4OjUwIn0.
        // -K8ByV3Fta2o65dHsd322sUz2eucJnp3kXU0d-dVieM

        String[] authHeaders = authHeader.substring(7).split("\\.");
        String tokenData = String.join(".", authHeaders[0], authHeaders[1]);

        // Signature trong token gửi từ Client lên
        String signature = authHeaders[2];

        // Signature được tạo ra từ tokenData từ Client gửi lên
        String authSignature = accountService.getSignature(tokenData);

        System.out.println(signature + "\n" + authSignature);

        // Nếu tokenData bị thay đổi thì lúc này signature được tạo ra cũng sẽ khác với signature cũ
        if (!signature.equals(authSignature)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

        ObjectMapper json = new ObjectMapper();
        Account account = json.readValue(jsonData, Account.class);
        System.out.println(account);

        return ResponseEntity.status(HttpStatus.OK).body("OK nha");
    }
}
