//package jp.co.dir.falcon.online.auth.security.service;
//
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jp.co.dir.falcon.online.auth.security.utils.AuthExceptionUtil;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
////カスタム認証失敗例外処理クラス
//@Component
//public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
//    private final Gson gson = new Gson();
//
//    @Override
//    public void commence(HttpServletRequest httpServletRequest,
//                         HttpServletResponse httpServletResponse,
//                         AuthenticationException authenticationException) throws IOException, ServletException {
//        String jsonResponse = gson.toJson(AuthExceptionUtil.getErrMsgByExceptionType(authenticationException));
//
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write(jsonResponse);
//    }
//}
//
//
