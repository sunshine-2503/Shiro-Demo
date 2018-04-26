package com.shaoming.comm.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.shaoming.comm.utils.MD5Util;
import com.shaoming.comm.vm.SysUserStatus;
import com.shaoming.sys.model.SysMenu;
import com.shaoming.sys.model.SysRoleMenu;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.model.SysUserRole;
import com.shaoming.sys.service.SysMenuService;
import com.shaoming.sys.service.SysRoleMenuService;
import com.shaoming.sys.service.SysUserRoleService;
import com.shaoming.sys.service.SysUserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Component
public class ShiroRealm extends AuthorizingRealm {
    private Logger logger = Logger.getLogger(ShiroRealm.class);

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
        // 获取当前登录用户
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        // 查询用户所属的角色集合
        List<SysUserRole> roles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().where("", user.getId()));
        Set<String> pemissionSet = new HashSet<>();
        for (SysUserRole role : roles) {
            // 查询角色所拥有的菜单权限
            List<SysRoleMenu> roleMenus = sysRoleMenuService.selectList(new EntityWrapper<SysRoleMenu>().where("role_id={0}", role.getRoleId()));
            for (SysRoleMenu roleMenu : roleMenus) {
                // 获取菜单信息
                SysMenu menu = sysMenuService.selectById(roleMenu.getMenuId());
                // 将菜单添加到权限列表
                pemissionSet.add(menu.getMenuCode());
            }
        }
        authorizationInfo.addStringPermissions(pemissionSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取前台传过来的用户名和密码
        String userName = (String)authenticationToken.getPrincipal();
//        String password = new String((char[]) authenticationToken.getCredentials());
        // 根据用户名查询用户信息
        SysUser user = sysUserService.selectOne(new EntityWrapper<SysUser>().where("user_name={0}", userName));

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
