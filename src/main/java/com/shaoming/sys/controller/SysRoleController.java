package com.shaoming.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shaoming.comm.vm.ResultVM;
import com.shaoming.comm.vm.Status;
import com.shaoming.sys.model.SysRole;
import com.shaoming.sys.model.SysRoleMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.model.SysUserRole;
import com.shaoming.sys.service.SysRoleMenuService;
import com.shaoming.sys.service.SysRoleService;
import com.shaoming.sys.service.SysUserRoleService;
import com.shaoming.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoMing on 2018/4/20
 */
@RestController
@RequestMapping("/web/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 添加系统角色
     *   注意：需要有角色管理权限
     */
    @RequiresPermissions({"power_role"})
    @PostMapping("/addSysRole")
    public ResultVM addSysRole(@RequestBody SysRole role){
        if (StringUtils.isEmpty(role.getRoleName()))
            return ResultVM.ok("请设置角色名称！");
        boolean bool = sysRoleService.insert(role);
        return bool ? ResultVM.ok("添加成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 查询系统角色列表
     *   注意：需要有角色管理权限
     */
    @RequiresPermissions({"power_role"})
    @GetMapping("/queryRoleList")
    public ResultVM queryRoleList(){
        List<SysRole> roleList = sysRoleService.selectList(new EntityWrapper<SysRole>().where("tb_status != {0}", Status.DELETE));
        return ResultVM.ok(roleList);
    }

    /**
     * 编辑角色
     */
    @RequiresPermissions({"power_role"})
    @PostMapping("/editSysRole")
    public ResultVM editRole(@RequestBody SysRole role){
        if (role.getId() == null)
            return ResultVM.error("传入参数有误！");
        if (StringUtils.isEmpty(role.getRoleName()))
            return ResultVM.error("角色名称必填！");
        if (role.getId() == 1)
            return ResultVM.error("该角色不能编辑！");
        boolean bool = sysRoleService.updateById(role);
        return bool ? ResultVM.ok("修改成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 删除角色
     */
    @RequiresPermissions({"power_role"})
    @GetMapping("/deleteSysRole")
    public ResultVM deleteRole(@RequestParam(name = "id") Integer id){
        if (id == 1)
            return ResultVM.error("该角色不能删除！");
        SysRole role = sysRoleService.selectById(id);
        if (role == null || Status.DELETE.equals(role.getTbStatus()))
            return ResultVM.error("该角色不存在或已删除！");
        role.setTbStatus(Status.DELETE);
        boolean bool = sysRoleService.updateById(role);
        // 删除该角色所绑定的菜单
        sysRoleMenuService.delete(new EntityWrapper<SysRoleMenu>().where("role_id={0}", id));
        // 删除该角色所绑定的用户
        sysUserRoleService.delete(new EntityWrapper<SysUserRole>().where("role_id={0}", id));
        return bool ? ResultVM.ok("删除成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 查询角色下的用户
     */
    @RequiresPermissions({"power_role"})
    @GetMapping("/queryRoleUser")
    public ResultVM queryRoleUser(@RequestParam(name = "id") Integer id){
        // 查询所有用户
        List<SysUser> userList = sysUserService.selectList(null);
        if (userList == null || userList.size()==0)
            return ResultVM.ok();
        Map<Integer, SysUser> userMap = new HashMap<>();
        for (SysUser user : userList) {
            userMap.put(user.getId(), user);
        }
        List<SysUser> results = new ArrayList<>();
        if (id == 1)
            results.add(userMap.get(1)); // 添加 admin 用户
        // 查询用户角色记录
        List<SysUserRole> userRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().where("role_id={0}", id));
        if (userRoles == null || userRoles.size() == 0)
            return ResultVM.ok(results);
        for (SysUserRole userRole : userRoles) {
            results.add(userMap.get(userRole.getUserId()));
        }
        return ResultVM.ok(results);
    }

    /**
     * 设置权限接口
     */
    @RequiresPermissions({"power_role"})
    @PostMapping("/editRoleMenu")
    public ResultVM editRoleMenu(@RequestParam(name = "roleId") Integer roleId, @RequestParam(name = "menuIds", required = false) List<Integer> menuIds){
        // 获取当前角色所拥有的权限
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(new EntityWrapper<SysRoleMenu>().where("role_id={0}", roleId));
        List<Integer> ids;
        if (!CollectionUtils.isEmpty(sysRoleMenus)){
            ids = new ArrayList<>();
            for (SysRoleMenu rm : sysRoleMenus) {
                ids.add(rm.getId());
            }
            // 删除用户当前所拥有的所有权限
            sysRoleMenuService.deleteBatchIds(ids);
        }
        if (CollectionUtils.isEmpty(menuIds))
            return ResultVM.ok("设置成功！");
        // 并且给角色新增权限
        for (Integer menuId : menuIds) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            sysRoleMenuService.insert(rm);
        }
        return ResultVM.ok("设置成功！");
    }
}
