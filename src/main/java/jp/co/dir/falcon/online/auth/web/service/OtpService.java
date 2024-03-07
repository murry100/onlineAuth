package jp.co.dir.falcon.online.auth.web.service;

public interface OtpService {
    String generateOTP(String phoneNumber);
    boolean validateOtp(String phoneNumber, String inputOtp);
}
