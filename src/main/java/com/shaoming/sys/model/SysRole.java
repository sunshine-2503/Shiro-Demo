package com.shaoming.sys.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * Created by ShaoMing on 2018/4/20
 */
@TableName("sys_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole {
    private Integer id; // ID
    private String roleName; // 角色 名称
    private String desc; // 描述
    private Integer order; // 排序（值越大，越靠前）
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
    private String tbStatus; // 状态
}
