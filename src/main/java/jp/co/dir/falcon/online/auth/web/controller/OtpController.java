package jp.co.dir.falcon.online.auth.web.controller;

import jp.co.dir.falcon.online.auth.web.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/otp/send")
    public String sendOtp(@RequestParam String phoneNumber) {
        return otpService.generateOTP(phoneNumber);
    }

    @GetMapping("/otp/validate")
    public boolean validateOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        return otpService.validateOtp(phoneNumber, otp);
    }
}
