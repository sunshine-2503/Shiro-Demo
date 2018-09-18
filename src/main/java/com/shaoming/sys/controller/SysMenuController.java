package com.shaoming.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.shaoming.comm.vm.ResultVM;
import com.shaoming.comm.vm.Status;
import com.shaoming.sys.model.SysMenu;
import com.shaoming.sys.model.SysRoleMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.model.SysUserRole;
import com.shaoming.sys.service.SysMenuService;
import com.shaoming.sys.service.SysRoleMenuService;
import com.shaoming.sys.service.SysUserRoleService;
import com.shaoming.sys.vo.MenuTreeVo;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Api(value = "sysMenu", description = "菜单管理")
@RestController
@RequestMapping("/web/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 查询所有菜单
     */
    @ApiOperation(value="查询所有菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 403, message = "没有访问权限")
    })
    @RequiresPermissions({"power_menu"})
    @GetMapping("/queryAllMenu")
    public ResultVM<List<SysMenu>> queryAllMenu() {
        List<SysMenu> sysMenus = sysMenuService.selectList(new EntityWrapper<SysMenu>().where("tb_status != {0}", Status.DELETE));
        return ResultVM.ok(sysMenus);
    }

    /**
     * 用户所拥有权限对应的权限树形菜单结构
     */
    @GetMapping("/queryUserMenuTree")
    public ResultVM<List<MenuTreeVo>> queryUserMenuTree(){
        // 获取当前登录用户信息
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        // 查找用户所对应的角色
        Wrapper<SysUserRole> wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0} and tb_status != {1}", user.getId(), Status.DELETE);
        wrapper.orderBy("role_id", true);
        List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(wrapper);
        if (CollectionUtils.isEmpty(sysUserRoles))
            return ResultVM.ok(null);
        // 判断用户是否为 “超级管理员”
        if (sysUserRoles.get(0).getRoleId() == 1)
            return ResultVM.ok(this.selectAllTree(null));
        // 查找所有权限菜单菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(null);
        List<Integer> menuIdList = new ArrayList<>();
        for (SysUserRole role : sysUserRoles) {
            for (SysRoleMenu rm : sysRoleMenus) {
                if (!role.getRoleId().equals(rm.getRoleId()))
                    continue;
                if (menuIdList.contains(rm.getMenuId()))
                    continue;
                menuIdList.add(rm.getMenuId());
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
    @ApiOperation(value="角色权限列表")
    @RequiresPermissions({"power_menu"})
    @GetMapping("/queryRoleMenuTree")
    public ResultVM<List<MenuTreeVo>> queryRoleMenuTree(@ApiParam(name = "roleId", value = "角色ID") Integer roleId){
        // 查找所有权限菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(new EntityWrapper<SysRoleMenu>().where("role_id={0} and tb_status != {1}", roleId, Status.DELETE));
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
    public ResultVM<List<MenuTreeVo>> queryMenuTree(){
        return ResultVM.ok(this.selectAllTree(null));
    }

    /**
     * 添加菜单
     *   注意：需要有菜单管理权限
     */
    @ApiOperation(value="添加菜单")

    @RequiresPermissions({"power_menu"})
    @PostMapping("/addMenu")
    public ResultVM<String> addMenu(@RequestBody SysMenu menu){
        if (StringUtils.isEmpty(menu.getMenuName()))
            return ResultVM.error("菜单名称必填！");
        if (StringUtils.isEmpty(menu.getMenuCode()))
            return ResultVM.error("路由名称必填！");
        SysMenu sysMenu = sysMenuService.selectOne(new EntityWrapper<SysMenu>().where("menu_name={0}", menu.getMenuName()));
        if (sysMenu != null){
            return ResultVM.error("已存在同名菜单！");
        }
        Integer parentId = menu.getParentId();
        if (parentId != null) {
            SysMenu parent = sysMenuService.selectById(parentId);
            menu.setMenuLevel(parent.getMenuLevel()+1);
        }
        boolean bool = sysMenuService.insert(menu);
        // TODO 绑定到超级管理员角色
        return bool ? ResultVM.ok("添加成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 编辑菜单
     *   注意：需要有菜单管理权限
     */
    @RequiresPermissions({"power_menu"})
    @PostMapping("/editMenu")
    public ResultVM<String> editMenu(@RequestBody SysMenu menu){
        if (StringUtils.isEmpty(menu.getMenuName()))
            return ResultVM.error("菜单名称必填！");
        if (StringUtils.isEmpty(menu.getMenuCode()))
            return ResultVM.error("路由名称必填！");
        SysMenu sysMenu = sysMenuService.selectById(menu.getId());
        if (sysMenu == null){
            return ResultVM.error("该菜单不存在或已删除！");
        }
        Integer parentId = menu.getParentId();
        if (parentId != null) {
            SysMenu parent = sysMenuService.selectById(parentId);
            menu.setMenuLevel(parent.getMenuLevel()+1);
        }
        boolean bool = sysMenuService.updateById(menu);
        return bool ? ResultVM.ok("添加成功！") : ResultVM.error("系统错误！");
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions({"power_menu"})
    @GetMapping("/deleteMenu")
    public ResultVM<String> deleteMenu(Integer menuId){
        SysMenu sysMenu = sysMenuService.selectById(menuId);
        if (sysMenu == null)
            return ResultVM.error("该菜单不存在或已删除！");
        sysMenu.setTbStatus(Status.DELETE);
        // 将该菜单设为删除状态
        sysMenuService.updateById(sysMenu);
        // 删除该菜单所绑定的角色
        sysRoleMenuService.delete(new EntityWrapper<SysRoleMenu>().where("menu_id={0}", menuId));
        return ResultVM.ok("删除成功！");
    }

    /**
     * 将角色所用有的权限设为已选中
     */
    private void selectRoleMenuTree(List<MenuTreeVo> menuTree, List<Integer> menuIdList) {
        if (menuTree==null || menuTree.size()==0) return;
        for (MenuTreeVo vo : menuTree) {
            if (vo.getMenuLevel() != 1)
                vo.setChecked(menuIdList.contains(vo.getId()));
            this.selectRoleMenuTree(vo.getChildrenMenu(), menuIdList);
        }
    }

    /**
     * 查询用户所拥有权限的菜单树形列表
     */
    private List<MenuTreeVo> selectMenuTree(List<MenuTreeVo> menuAllTree, List<Integer> menuIdList){
        if (CollectionUtils.isEmpty(menuAllTree) || CollectionUtils.isEmpty(menuIdList)) return null;
        List<MenuTreeVo> resultTree = new ArrayList<>();
        MenuTreeVo result;
        for (MenuTreeVo vo : menuAllTree) {
            result = new MenuTreeVo();
            if (!menuIdList.contains(vo.getId())) continue;
            BeanUtils.copyProperties(vo, result);
            result.setChildrenMenu(this.selectMenuTree(vo.getChildrenMenu(), menuIdList));
            result.setChecked(true);
            resultTree.add(result);
        }
        return resultTree;
    }

    /**
     * 获取树形结构菜单
     */
    private List<MenuTreeVo> selectAllTree(MenuTreeVo parent){
        Integer parentId = parent == null ? 0 : parent.getId();
        Wrapper<SysMenu> wrapper = new EntityWrapper<>();
        wrapper.where("`parent_id`={0} and `tb_status` != {1}", parentId, Status.DELETE);
        wrapper.orderBy("`order`",  false);
        List<SysMenu> children = sysMenuService.selectList(wrapper);
        if (CollectionUtils.isEmpty(children))
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