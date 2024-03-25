package jp.co.dir.falcon.online.auth.runner;

import jp.co.dir.falcon.online.auth.common.utils.RedisUtil;
import jp.co.dir.falcon.online.auth.web.entity.ApiPermissions;
import jp.co.dir.falcon.online.auth.web.entity.RolePermissions;
import jp.co.dir.falcon.online.auth.web.mapper.SysPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MyStartupRunner implements CommandLineRunner {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public void run(String... args) throws Exception {
        //roleKey -> url
        List<ApiPermissions> apiPermissions = sysPermissionMapper.selectPermissionAllList();
        Map<Integer, List<ApiPermissions>> roleMap = new HashMap<>();
        apiPermissions.stream().forEach(permissions -> {
            Integer roleId = permissions.getRoleId();
            if(!roleMap.containsKey(roleId)){
                roleMap.put(permissions.getRoleId(), new ArrayList<>());
            }
            roleMap.get(roleId).add(permissions);
        });
        redisUtil.set("Authorization", roleMap);
    }
}
