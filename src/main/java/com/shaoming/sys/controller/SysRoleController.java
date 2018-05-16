package com.shaoming.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shaoming.comm.vm.ResultVM;
import com.shaoming.sys.model.SysRole;
import com.shaoming.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ShaoMing on 2018/4/20
 */
@RestController
@RequestMapping("/web/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 添加系统角色
     *   注意：需要有角色管理权限
     */
    @RequiresPermissions({"power_role"})
    @PostMapping("/addSysRole")
    public ResultVM addSysRole(@RequestBody SysRole role){
        if (StringUtils.isEmpty(role.getRoleName()))
            return ResultVM.ok("请设置角色名称！");
        Boolean bool = sysRoleService.insert(role);
        return bool ? ResultVM.ok("添加成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 查询系统角色列表
     *   注意：需要有角色管理权限
     */
    @RequiresPermissions({"power_role"})
    @GetMapping("/queryRoleList")
    public ResultVM queryRoleList(){
        List<SysRole> roleList = sysRoleService.selectList(new EntityWrapper<SysRole>().where("tb_status != '删除'"));
        return ResultVM.ok(roleList);
    }

}
