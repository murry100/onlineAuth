//package jp.co.dir.falcon.online.auth.security.service;
//
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jp.co.dir.falcon.online.auth.security.utils.AuthExceptionUtil;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//    private final Gson gson = new Gson();
//
//    @Override
//    public void handle(HttpServletRequest httpServletRequest,
//                       HttpServletResponse httpServletResponse,
//                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
//
//// 使用Gson序列化异常信息
//        String jsonResponse = gson.toJson(AuthExceptionUtil.getErrMsgByExceptionType(accessDeniedException));
//
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.getWriter().write(jsonResponse);
//    }
//}
//
//
