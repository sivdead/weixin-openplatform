package com.github.sivdead.wx.open.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.chanjar.weixin.common.error.WxErrorException;

import static com.github.sivdead.wx.open.constant.AppConstant.result_code_error;

@Getter
@Setter
@AllArgsConstructor
public class BusinessException extends RuntimeException {

    private String code;

    private String msg;

    public BusinessException(String message) {
        super(message);
        this.code = result_code_error;
        this.msg = message;
    }

    public BusinessException(WxErrorException e) {
        super(e.getError().getErrorMsg());
        this.code = String.valueOf(e.getError().getErrorCode());
        this.msg = e.getError().getErrorMsg();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = String.valueOf(code);
        this.msg = message;
    }
}
