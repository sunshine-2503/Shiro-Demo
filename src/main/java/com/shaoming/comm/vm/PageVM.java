package com.shaoming.comm.vm;

import lombok.Data;

/**
 * Created by ShaoMing on 2018/5/16
 */
@Data
public class PageVM {
    private Integer page; //当前页码
    private Integer size; //每页显示的数目
    private Integer total; //总条数
    private Object records; //记录
}
