package com.shaoming.sys.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by ShaoMing on 2018/4/20
 */
@TableName("sys_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {
    private Integer id; //ID
    private String userName; //用户名
    private String password; //密码
    private String email; //邮件
    private String mobile; //手机
    private String headIcon; //头像
    private String salt; //盐
    private Timestamp createTime; //创建时间
    private Timestamp updateTime; //更新时间
    private String tbStatus; //状态：正常，正常；锁定，锁定；删除，删除；
}
