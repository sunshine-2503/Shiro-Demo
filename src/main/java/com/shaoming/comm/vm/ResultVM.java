package com.shaoming.comm.vm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by ShaoMing on 2018/4/20
 */
@Data
public class ResultVM<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "错误码")
    private Integer code; // 200:成功；500:失败

    @ApiModelProperty(value = "错误信息")
    private String msg; // 错误信息

    @ApiModelProperty(value = "返回结果")
    private T result; // 结果

    public ResultVM() { }

    public ResultVM(Integer code) {
        this.code = code;
    }

    public ResultVM(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVM(Integer code, T result) {
        this.code = code;
        this.result = result;
    }

    public ResultVM(Integer code, String msg, T result) {
        this.code = code;
        this.msg=msg;
        this.result = result;
    }

    public ResultVM(String msg, T result) {
        this.msg = msg;
        this.result = result;
    }

    public ResultVM(T result) {
        this.result = result;
    }

    public static<T> ResultVM<T> ok() {
        return new ResultVM<>(200);
    }

    public static<T> ResultVM<T> ok(String msg) {
        return ok(200, msg);
    }

    public static<T> ResultVM<T> ok(Integer code, String msg) {
        return new ResultVM<>(code, msg);
    }

    public static<T> ResultVM<T> ok(T result) {
        return new ResultVM<>(200, result);
    }

    public static<T> ResultVM<T> ok(String msg, T result) {
        return new ResultVM<>(200, msg, result);
    }

    public static<T> ResultVM<T> error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static<T> ResultVM<T> error(String msg) {
        return error(500, msg);
    }

    public static<T> ResultVM<T> error(Integer code, String msg) {
        return new ResultVM<>(code, msg);
    }

    public static<T> ResultVM<T> info(Boolean bool) {
        if (bool) {
            return ResultVM.ok();
        } else {
            return ResultVM.error();
        }
    }

}
