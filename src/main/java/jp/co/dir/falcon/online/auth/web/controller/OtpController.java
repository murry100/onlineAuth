package jp.co.dir.falcon.online.auth.web.controller;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import jp.co.dir.falcon.online.auth.web.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

@RestController
@RequestMapping("/auth")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/ver1/otp/send")
    public String sendOtp(@RequestParam String phoneNumber) {
        return otpService.generateOTP(phoneNumber);
    }

    @GetMapping("/ver1/login/mfa")
    public ApiResult validateOtp(@RequestParam String phoneNumber, @RequestParam String otp, ServerWebExchange exchange) {
        return otpService.validateOtp(phoneNumber, otp, exchange);
    }
}
