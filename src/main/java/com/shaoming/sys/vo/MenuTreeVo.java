package com.shaoming.sys.vo;

import com.shaoming.sys.model.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * Created by ShaoMing on 2018/5/11
 */
@Data
public class MenuTreeVo extends SysMenu {
    private List<MenuTreeVo> childrenMenu; // 子级菜单
    private Boolean isChecked; // 是否选中
}
