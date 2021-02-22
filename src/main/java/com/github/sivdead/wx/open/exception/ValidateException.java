package com.github.sivdead.wx.open.exception;

import com.alibaba.fastjson.JSON;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidateException extends BusinessException {
    private static final long serialVersionUID = 3822431451673643694L;
    private final Map<String, String> errors;

    public ValidateException() {
        this(ExceptionCode.VALIDATION_FAIL);
    }

    public ValidateException(String code, String msg) {
        super(code, msg);
        this.errors = new HashMap<>();
    }

    public ValidateException(String msg) {
        super(ExceptionCode.VALIDATION_FAIL.getCode(), msg);
        this.errors = new HashMap<>();
    }

    public ValidateException(ExceptionCode status) {
        super(status.getCode(), status.getMsg());
        this.errors = new HashMap<>();
    }

    public ValidateException(Map<String, String> errors) {
        super(ExceptionCode.VALIDATION_FAIL.getCode(), ExceptionCode.VALIDATION_FAIL.getMsg());
        this.errors = new HashMap<>();
        if (errors != null) {
            this.errors.putAll(errors);
        }

    }

    public ValidateException(ConstraintViolationException ex) {
        super(ExceptionCode.VALIDATION_FAIL.getCode(), ExceptionCode.VALIDATION_FAIL.getMsg());
        this.errors = new HashMap<>();
        this.errors.putAll(this.toMap(ex.getConstraintViolations()));
    }

    public <T> ValidateException(Set<ConstraintViolation<T>> violations) {
        super(ExceptionCode.VALIDATION_FAIL.getCode(), ExceptionCode.VALIDATION_FAIL.getMsg());
        this.errors = new HashMap<>();
        this.errors.putAll(this.toErrorMap(violations));
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public ValidateException addError(String name, String message) {
        this.errors.put(name, message);
        return this;
    }

    private <T> Map<String, String> toErrorMap(Set<ConstraintViolation<T>> violations) {
        Map<String, String> errs = new HashMap<>();
        if (!violations.isEmpty()) {

            for (ConstraintViolation<T> violation : violations) {
                errs.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            this.errors.putAll(errs);
        }

        return errs;
    }

    private Map<String, String> toMap(Set<ConstraintViolation<?>> violations) {
        Map<String, String> errs = new HashMap<>();
        if (!violations.isEmpty()) {

            for (ConstraintViolation<?> violation : violations) {
                errs.put(violation.getPropertyPath().toString(), violation.getMessage());
            }

            this.errors.putAll(errs);
        }

        return errs;
    }

    public String getMessage() {
        try {
            return JSON.toJSONString(this.errors);
        } catch (Exception var2) {
            return null;
        }
    }
}
