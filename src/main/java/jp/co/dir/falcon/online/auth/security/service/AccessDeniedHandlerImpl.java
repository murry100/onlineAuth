package jp.co.dir.falcon.online.auth.security.service;

import com.alibaba.fastjson.JSON;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.dir.falcon.online.auth.common.utils.WebUtils;
import jp.co.dir.falcon.online.auth.security.utils.AuthExceptionUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//自定义授权失败异常处理类
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        System.out.println("AccessDeniedHandler:暂无权限");
        WebUtils.rednerString(httpServletResponse, JSON.toJSONString(AuthExceptionUtil.getErrMsgByExceptionType(accessDeniedException)));

    }
}


