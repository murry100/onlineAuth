package jp.co.dir.falcon.online.auth.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)//链式; 存取器。通过该注解可以控制getter和setter方法的形式。
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 915478504870211231L;

    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Integer userId;

    //账号
    private String account;

    //用户名
    private String userName;

    //用户密码
    private String password;

    //上一次登录时间
    private Date lastLoginTime;

    //账号是否可用。默认为1（可用）
    private Boolean enabled;

    //是否过期。默认为1（没有过期）
    private Boolean accountNotExpired;

    //账号是否锁定。默认为1（没有锁定）
    private Boolean accountNotLocked;

    private Date allowAt;

    private Integer errorNum;

    //证书（密码）是否过期。默认为1（没有过期）
    private Boolean credentialsNotExpired;

    //创建时间
    private Date createTime;

    //修改时间
    private Date updateTime;

}

