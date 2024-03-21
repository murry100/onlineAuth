//package jp.co.dir.falcon.online.auth.web.controller;
//
//import jp.co.dir.falcon.online.auth.common.api.ApiCode;
//import jp.co.dir.falcon.online.auth.common.api.ApiResult;
//import jp.co.dir.falcon.online.auth.web.param.LoginUserParam;
//import jp.co.dir.falcon.online.auth.web.service.LogService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class SysUserControllerTest {
//
//    @Mock
//    private LogService logService;
//
//    @InjectMocks
//    private SysUserController sysUserController;
//
//    @Test
//    void login() {
//        // Arrange
//        final String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTA3Mzk3MzksInNlY29uZCI6MTIwMH0.Oy3_Odr341qCubZSH28kfTtPHfdP4aOtO6g5UUp2XZE";
//        LoginUserParam loginUserParam = new LoginUserParam();
//        loginUserParam.setUserName("888");
//        loginUserParam.setPassword("123456");
//        Map<String, String> payloadMap = new HashMap<>();
//        payloadMap.put("id_token", jwt);
//        ApiResult expectedResult = ApiResult.success(jwt);
//
//        // Act & Assert
//        when(logService.login(any(LoginUserParam.class))).thenReturn(expectedResult);
//        ApiResult result = sysUserController.login(loginUserParam);
//        assertEquals(jwt, result.getData());
//    }
//}