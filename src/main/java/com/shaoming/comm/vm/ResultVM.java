package com.shaoming.comm.vm;

import java.io.Serializable;

/**
 * Created by ShaoMing on 2018/4/20
 */
public class ResultVM implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code; // 200:成功；500:失败
    private String msg; // 错误信息
    private Object result; // 结果集

    public ResultVM() { }

    public ResultVM(Integer code) {
        this.code = code;
    }

    public ResultVM(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVM(Integer code, Object result) {
        this.code = code;
        this.result = result;
    }

    public ResultVM(Integer code, String msg, Object result) {
        this.code = code;
        this.msg=msg;
        this.result = result;
    }

    public ResultVM(String msg, Object result) {
        this.msg = msg;
        this.result = result;
    }

    public ResultVM(Object result) {
        this.result = result;
    }

    public static ResultVM error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static ResultVM error(String msg) {
        return error(500, msg);
    }

    public static ResultVM error(Integer code, String msg) {
        return new ResultVM(code, msg);
    }

    public static ResultVM ok() {
        return new ResultVM(200);
    }

    public static ResultVM ok(String msg) {
        return ok(200, msg);
    }

    public static ResultVM ok(Integer code, String msg) {
        return new ResultVM(code, msg);
    }

    public static ResultVM ok(Object result) {
        return new ResultVM(200, result);
    }

    public static ResultVM ok(String msg, Object result) {
        return new ResultVM(200, msg, result);
    }

    public static ResultVM info(Boolean ret) {
        if (ret) {
            return ResultVM.ok();
        } else {
            return ResultVM.error();
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
