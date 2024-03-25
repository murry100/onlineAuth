package jp.co.dir.falcon.online.auth.web.service;

import jp.co.dir.falcon.online.auth.common.api.ApiResult;
import org.springframework.web.server.ServerWebExchange;

public interface OtpService {
    String generateOTP(String phoneNumber);

    ApiResult validateOtp(String inputOtp, ServerWebExchange exchange);
}
