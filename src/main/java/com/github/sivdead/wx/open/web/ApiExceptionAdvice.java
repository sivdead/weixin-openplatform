package com.github.sivdead.wx.open.web;

import com.github.sivdead.wx.open.exception.BusinessException;
import com.github.sivdead.wx.open.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ApiExceptionAdvice {

    @ExceptionHandler({BusinessException.class})
    @ResponseBody
    public R<?> handleBusinessException(BusinessException e) {
        return R.failed(e);
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R<?> handleBindExceptionException(BindException ex) {
        return this.extractMsg(ex.getBindingResult());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R<?> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        return this.extractMsg(ex.getBindingResult());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R<?> handleValidateException(ConstraintViolationException ex) {
        ValidateException vex = new ValidateException(ex);
        log.debug("数据验证失败：{}", vex.getMessage());
        return R.fail(vex.getCode(), vex.getMessage(), vex.getErrors());
    }


    private R<?> extractMsg(BindingResult bindingResult) {
        ValidateException vex = new ValidateException();
        if (bindingResult.hasFieldErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError err : errors) {
                vex.addError(err.getField(), err.getDefaultMessage());
            }
        }

        log.debug("数据验证失败：{}", vex.getMessage());
        return R.fail(vex.getCode(), vex.getMessage(), vex.getErrors());
    }
}
