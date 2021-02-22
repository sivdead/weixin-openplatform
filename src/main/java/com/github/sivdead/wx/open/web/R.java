package com.github.sivdead.wx.open.web;

import com.github.sivdead.wx.open.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.github.sivdead.wx.open.constant.AppConstant.result_code_success;
import static com.github.sivdead.wx.open.constant.AppConstant.result_msg_success;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    private String code;

    private String message;

    private T data;

    public static <T> R<T> ok(T data) {
        return new R<>(result_code_success, result_msg_success, data);
    }

    public static <T> R<T> ok() {
        return new R<>(result_code_success, result_msg_success, null);
    }

    public static <T> R<T> failed(BusinessException exception) {
        return new R<>(exception.getCode(), exception.getMsg(), null);
    }

    public static <T> R<T> fail(String code, String msg, T t) {
        return new R<>(code, msg, t);
    }

}
