package com.shaoming.sys.controller;

import com.shaoming.comm.vm.ResultVM;
import com.shaoming.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Slf4j
@RestController
@RequestMapping("/web/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVM login(@RequestParam("userName") String userName,
                          @RequestParam("password") String password,
                          @RequestParam(name = "rememberMe", defaultValue = "false") Boolean rememberMe){
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            return ResultVM.error("用户名不存在！");
        } catch (IncorrectCredentialsException e) {
            return ResultVM.error("用户名或密码错误！");
        } catch (ExcessiveAttemptsException e) {
            return ResultVM.error("密码输入错误次数过多，请稍后再试！");
        } catch (LockedAccountException e) {
            return ResultVM.error("用户账号已被锁定！");
        }
        return ResultVM.ok("登录成功！");
    }

    /**
     * 配置未登录跳转的方法
     */
    @GetMapping("/noLogin")
    public ResultVM noLogin(){
        return ResultVM.error(300,"请先登录！");
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public ResultVM logout(){
        Subject subject = SecurityUtils.getSubject();
        // 判断用户是否已登录
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return ResultVM.ok("登出成功！");
    }

}
