package jp.co.dir.falcon.online.auth.common.utils;

import jakarta.servlet.http.HttpServletResponse;

public class WebUtils {
    public static String rednerString(HttpServletResponse response, String content) {
        try {
            response.setStatus(200);
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


