package com.shaoming.comm.config;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     * 文档：http://mp.baomidou.com
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType(DBType.MYSQL.getDb());
        return paginationInterceptor;
    }

}
