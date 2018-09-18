package com.shaoming.sys.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by ShaoMing on 2018/4/20
 */
@TableName("sys_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu {
    @ApiModelProperty(value = "菜单ID（新增不用传）")
    private Integer id;

    @ApiModelProperty(value = "父级ID")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "路由名称")
    private String menuCode;

    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "菜单层级")
    private Integer menuLevel;

    @ApiModelProperty(value = "排序（值越大，越靠前）")
    private Integer order;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "状态")
    private String tbStatus;

}
