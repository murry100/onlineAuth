package jp.co.dir.falcon.online.auth.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("users")
public class Users implements Serializable {
    private static final long serialVersionUID = 915478504870211231L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer userId;

    private String loginId;

    private String password;

    private Date passwordUpdatedDatetime;

    private String email;

    private String phoneNumber;

    private String userType;

    private String emailVerificationStatus;

    private String identityVerificationStatus;

    private String reviewStatus;

    private String userStatus;

    private String activated;

    private String customerNumber;

    private Integer loginErrCnt;

    private Date loginUnlockDatetime;

    private Integer loginCnt;

    private Integer loginOkCnt;

    private Date loginFirstYmd;

    private Timestamp loginDatetimeLast;

    private Timestamp logoutDatetime;

    private String logicalDeletionFlag;

    private Timestamp logicalDeletionDatetime;

    private Timestamp createdDatetime;

    private String createdBy;

    private Timestamp updatedDatetime;

    private String updatedBy;

}
