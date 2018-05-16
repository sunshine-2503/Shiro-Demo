package com.shaoming.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shaoming.comm.vm.ResultVM;
import com.shaoming.sys.model.SysMenu;
import com.shaoming.sys.model.SysRoleMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.model.SysUserRole;
import com.shaoming.sys.service.SysMenuService;
import com.shaoming.sys.service.SysRoleMenuService;
import com.shaoming.sys.service.SysUserRoleService;
import com.shaoming.sys.vo.MenuTreeVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaoMing on 2018/4/20
 */
@RestController
@RequestMapping("/web/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

//    /**
//     * 查询所有菜单
//     */
//    @RequiresPermissions({"power_menu"})
//    @GetMapping("/queryAllMenu")
//    public ResultVM queryAllMenu() {
//        List<SysMenu> sysMenus = sysMenuService.selectList(null);
//        return ResultVM.ok(sysMenus);
//    }

    /**
     * 用户所拥有权限对应的权限树形菜单结构
     */
    @GetMapping("/queryUserMenuTree")
    public ResultVM queryUserMenuTree(){
        // 获取当前登录用户信息
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        // 查找用户所对应的角色
        List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().where("user_id={0} and tb_status != '删除'", user.getId()).orderBy("role_id", true));
        if (sysUserRoles==null || sysUserRoles.size()==0)
            return ResultVM.ok(null);
        // 判断用户是否为 “超级管理员”
        if (sysUserRoles.get(0).getRoleId() == 1)
            return ResultVM.ok(this.selectAllTree(null));
        // 查找所有权限菜单菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(null);
        List<Integer> menuIdList = new ArrayList<>();
        for (SysUserRole role : sysUserRoles) {
            for (SysRoleMenu rm : sysRoleMenus) {
                if (role.getRoleId().equals(rm.getRoleId())){
                    if (menuIdList.contains(rm.getMenuId()))
                        continue;
                    menuIdList.add(rm.getMenuId());
                }
            }
        }
        // 查询所有菜单
        List<MenuTreeVo> menuTree = this.selectAllTree(null);
        return ResultVM.ok(this.selectMenuTree(menuTree, menuIdList));
    }

    /**
     * 获取角色权限列表
     *   注意：需要有菜单管理权限
     */
    @RequiresPermissions({"power_menu"})
    @GetMapping("/queryRoleMenuTree")
    public ResultVM queryRoleMenuTree(@RequestParam(name = "roleId") Integer roleId){
        // 查找所有权限菜单菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(new EntityWrapper<SysRoleMenu>().where("role_id={0} and tb_status != '删除'", roleId));
        List<Integer> menuIdList = new ArrayList<>();
        for (SysRoleMenu rm : sysRoleMenus) {
            if (menuIdList.contains(rm.getMenuId()))
                continue;
            menuIdList.add(rm.getMenuId());
        }
        // 查询所有菜单
        List<MenuTreeVo> menuTree = this.selectAllTree(null);
        this.selectRoleMenuTree(menuTree, menuIdList);
        return ResultVM.ok(menuTree);
    }

    /**
     * 获取菜单管理列表
     *   注意：需要有菜单管理权限
     */
    @RequiresPermissions({"power_menu"})
    @GetMapping("/queryMenuTree")
    public ResultVM queryMenuTree(){
        return ResultVM.ok(this.selectAllTree(null));
    }

    /**
     * 将角色所用有的权限设为已选中
     */
    private void selectRoleMenuTree(List<MenuTreeVo> menuTree, List<Integer> menuIdList) {
        if (menuTree==null || menuTree.size()==0 || menuIdList==null || menuIdList.size()==0) return;
        for (MenuTreeVo vo : menuTree) {
            vo.setIsChecked(menuIdList.contains(vo.getId()));
            vo.setChildrenMenu(this.selectMenuTree(vo.getChildrenMenu(), menuIdList));
        }
    }

    /**
     * 查询用户所拥有权限的菜单树形列表
     */
    private List<MenuTreeVo> selectMenuTree(List<MenuTreeVo> menuAllTree, List<Integer> menuIdList){
        if (menuAllTree==null || menuAllTree.size()==0 || menuIdList==null || menuIdList.size()==0) return null;
        List<MenuTreeVo> resultTree = new ArrayList<>();
        MenuTreeVo result;
        for (MenuTreeVo vo : menuAllTree) {
            result = new MenuTreeVo();
            if (!menuIdList.contains(vo.getId())) continue;
            BeanUtils.copyProperties(vo, result);
            result.setChildrenMenu(this.selectMenuTree(vo.getChildrenMenu(), menuIdList));
            result.setIsChecked(true);
            resultTree.add(result);
        }
        return resultTree;
    }

    /**
     * 获取树形结构菜单
     */
    private List<MenuTreeVo> selectAllTree(MenuTreeVo parent){
        Integer parentId = parent == null ? 0 : parent.getId();
        List<SysMenu> children = sysMenuService.selectList(new EntityWrapper<SysMenu>().where("`parent_id`={0} and tb_status != '删除'", parentId).orderBy("`order`",  false));
        if (children==null || children.size()==0)
            return null;
        List<MenuTreeVo> voList = new ArrayList<>();
        MenuTreeVo vo;
        for (SysMenu menu : children) {
            vo = new MenuTreeVo();
            BeanUtils.copyProperties(menu, vo);
            vo.setChildrenMenu(this.selectAllTree(vo));
            voList.add(vo);
        }
        return voList;
    }

}