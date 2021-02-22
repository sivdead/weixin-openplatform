package com.github.sivdead.wx.open.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    VALIDATION_FAIL("1000", "数据验证失败"),
    PARAMETER_ERROR("1001", "参数错误"),
    MEDIA_GROUP_NOT_FOUND("1002", "素材分组不存在"),
    ALREADY_BIND_OTHER_OPEN_PLATFORM("1003", "已绑定其他开放平台"),
    TAG_NAME_DUPLICATED("1004","标签名称重复"),
    THIRD_PARTY_USER_NOT_FOUND("1005","用户不存在"),
    TAG_NOT_FOUND("1006","标签不存在"),
    ;

    private final String code;

    private final String msg;

    public BusinessException toBusinessException() {
        return new BusinessException(code, msg);
    }

}
