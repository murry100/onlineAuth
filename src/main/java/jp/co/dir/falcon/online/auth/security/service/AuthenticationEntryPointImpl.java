package jp.co.dir.falcon.online.auth.security.service;

import com.alibaba.fastjson.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.dir.falcon.online.auth.common.utils.WebUtils;
import jp.co.dir.falcon.online.auth.security.utils.AuthExceptionUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//自定义认证失败异常处理类
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException authenticationException) throws IOException, ServletException {
        System.out.println("AuthenticationEntryPoint:用户未登录");
        WebUtils.rednerString(httpServletResponse, JSON.toJSONString(AuthExceptionUtil.getErrMsgByExceptionType(authenticationException)));
    }
}


