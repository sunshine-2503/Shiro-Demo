package com.shaoming.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shaoming.comm.exception.ErrorException;
import com.shaoming.sys.mapper.SysUserMapper;
import com.shaoming.sys.model.SysUser;
import com.shaoming.sys.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public String test(Integer num) throws Exception {
        if (num.equals(1)) {
            throw new ErrorException("抛出异常！");
        }
        return "测试";
    }
}
