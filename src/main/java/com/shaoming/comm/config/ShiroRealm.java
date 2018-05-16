package com.shaoming.comm.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shaoming.comm.vm.SysUserStatus;
import com.shaoming.sys.model.SysMenu;
import com.shaoming.sys.model.SysRoleMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.model.SysUserRole;
import com.shaoming.sys.service.SysMenuService;
import com.shaoming.sys.service.SysRoleMenuService;
import com.shaoming.sys.service.SysUserRoleService;
import com.shaoming.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysMenuService sysMenuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 拥有的权限集合
        Set<String> permissionSet = new HashSet<>();
        // 获取当前登录用户
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        // 获取所有菜单
        List<SysMenu> sysMenus = sysMenuService.selectList(null);
        // 判断用户是否未超级管理员, 若为超级管理员将赋予所有权限
        if (user.getId() == 1) {
            for (SysMenu menu : sysMenus) {
                permissionSet.add(menu.getMenuCode());
            }
            authorizationInfo.addStringPermissions(permissionSet);
            return authorizationInfo;
        }
        // 拼装menuMap
        Map<Integer, SysMenu> menuMap = new HashMap<>();
        for (SysMenu menu : sysMenus) {
            menuMap.put(menu.getId(), menu);
        }

        // 查询用户所属的角色集合
        List<SysUserRole> roles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().where("user_id={0}", user.getId()));
        // 获取所有角色菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.selectList(null);
        for (SysUserRole role : roles) {
            for (SysRoleMenu roleMenu : sysRoleMenus) {
                if (!role.getRoleId().equals(roleMenu.getRoleId()))
                    continue;
                // 将菜单添加到权限列表
                permissionSet.add(menuMap.get(roleMenu.getMenuId()).getMenuCode());
            }
        }
        authorizationInfo.addStringPermissions(permissionSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取前台传过来的用户名和密码
        String userName = (String)authenticationToken.getPrincipal();
//        String password = new String((char[]) authenticationToken.getCredentials());
        // 根据用户名查询用户信息
        SysUser user = sysUserService.selectOne(new EntityWrapper<SysUser>().where("user_name={0} and tb_status != '删除'", userName));

        // 判断用户是否存在
        if(user==null || user.getId()==null)
            throw new UnknownAccountException("用户名不存在！");

        // 判断用户是否被锁定
        if(SysUserStatus.LOCKED.equals(user.getTbStatus()))
            throw new LockedAccountException("该用户已被锁定！");

        // 此处是获取数据库内的账号、密码、盐值，保存到登陆信息info中
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
        return authenticationInfo;
    }

}
