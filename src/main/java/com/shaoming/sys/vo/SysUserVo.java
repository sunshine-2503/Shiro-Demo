package com.shaoming.sys.vo;

import com.shaoming.sys.model.SysRole;
import com.shaoming.sys.model.SysUser;
import lombok.Data;

import java.util.List;

/**
 * Created by ShaoMing on 2018/5/16
 */
@Data
public class SysUserVo extends SysUser {

    // 用户所属角色
    List<SysRole> roleList;

}
