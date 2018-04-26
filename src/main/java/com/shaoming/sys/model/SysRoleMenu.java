package com.shaoming.sys.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * Created by ShaoMing on 2018/4/20
 */
@TableName("sys_role_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleMenu {
    private Integer id; // ID
    private Integer roleId; // 角色id
    private Integer menuId; // 菜单id
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
    private String tbStatus; // 状态
}
