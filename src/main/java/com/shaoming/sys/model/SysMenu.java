package com.shaoming.sys.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * Created by ShaoMing on 2018/4/20
 */
@TableName("sys_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu {
    private Integer id; // ID
    private Integer parentId; // 父级id
    private String menuName; // 菜单名称
    private String menuCode; // 菜单CODE
    private String menuIcon; // 菜单图标
    private Integer menuLevel; // 菜单层级
    private Integer order; // 排序（值越大，越靠前）
    private Timestamp createTime; // 创建时间
    private Timestamp updateTime; // 更新时间
    private String tbStatus; // 状态
}
