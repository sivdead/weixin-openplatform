package com.github.sivdead.wx.open.util;


import cn.hutool.core.collection.CollectionUtil;
import com.github.sivdead.wx.open.exception.BusinessException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

import static com.github.sivdead.wx.open.exception.ExceptionCode.PARAMETER_ERROR;

/**
 * @author jingwen
 */
@Component
public class ValidateUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> void validate(T t) {

        validate(t, new Class[]{});
    }

    public static <T> void validate(T t, Class<?>... groups) {
        Validator validator = applicationContext.getBean(Validator.class);
        Set<ConstraintViolation<T>> violations = validator.validate(t, groups);
        if (CollectionUtil.isNotEmpty(violations)) {
            throw new ConstraintViolationException("参数校验失败",violations);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ValidateUtil.applicationContext = applicationContext;
    }
}
