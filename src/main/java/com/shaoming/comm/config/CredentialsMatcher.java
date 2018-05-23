package com.shaoming.comm.config;

import com.shaoming.comm.utils.MD5Util;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.util.ByteSource;

/**
 * Created by ShaoMing on 2018/4/20
 *
 * CredentialsMatcher是一个接口，功能就是用来匹配用户登录使用的令牌和数据库中保存的用户信息是否匹配。
 * 这个类在ShiroConfiguration中注入，并设置为注入的Realm类的属性。
 * 也可以不写这个类，直接在ShiroConfiguration用HashedCredentialsMatcher注入。
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 进行密码的比对,验证密码是否正确
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info){
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获得用户输入的密码，可以用加盐(salt)的方式去检验
        String inPassword = new String(token.getPassword());
        ByteSource credentialsSalt = ((SimpleAuthenticationInfo) info).getCredentialsSalt();
        byte[] saltBytes = credentialsSalt.getBytes();
        String salt = new String(saltBytes);
        // 密码加密（密码+盐+双重MD5）
        String md5Pwd = MD5Util.md5(MD5Util.md5(inPassword+salt));
        //获得数据库中的密码
        String daPassword = (String) info.getCredentials();
        //进行密码的比对
        return this.equals(md5Pwd, daPassword);
    }

}
