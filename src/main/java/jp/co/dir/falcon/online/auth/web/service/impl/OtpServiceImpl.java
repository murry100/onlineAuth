package jp.co.dir.falcon.online.auth.web.service.impl;

import jp.co.dir.falcon.online.auth.web.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOTP(String phoneNumber) {
        String otp = String.valueOf(100000 + secureRandom.nextInt(900000)); // 6桁のOTP生成
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(phoneNumber, otp, 1, TimeUnit.MINUTES); // Redisに1分間保存
        sendOtpViaSms(phoneNumber, otp); // SMSでOTPを送信（このメソッドは具体的な実装に依存）
        return otp;
    }

    private void sendOtpViaSms(String phoneNumber, String otp) {
        // SMS送信の実装。サービスプロバイダーのAPIを使用
    }

    public boolean validateOtp(String phoneNumber, String inputOtp) {
        String storedOtp = redisTemplate.opsForValue().get(phoneNumber);
        return storedOtp != null && storedOtp.equals(inputOtp);
    }
}