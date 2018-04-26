package com.shaoming.sys.controller;

import com.shaoming.comm.vm.ResultVM;
import com.shaoming.sys.model.SysMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.service.SysMenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * Created by ShaoMing on 2018/4/20
 */
@RestController
@RequestMapping("/web/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询所有菜单
     */
    @RequiresPermissions({"power_menu"})
    @GetMapping("/queryAllMenu")
    public ResultVM queryAllMenu(){
        // 获取当前登录用户信息
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();

        List<SysMenu> sysMenus = sysMenuService.selectList(null);
        return ResultVM.ok(sysMenus);
    }

}
