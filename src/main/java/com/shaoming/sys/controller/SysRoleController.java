package com.shaoming.sys.controller;

import com.shaoming.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ShaoMing on 2018/4/20
 */
@RestController
@RequestMapping("/web/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

}
