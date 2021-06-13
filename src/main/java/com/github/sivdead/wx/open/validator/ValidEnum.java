package com.github.sivdead.wx.open.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class})
public @interface ValidEnum {

    Class<? extends IEnum> enumClass();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "不是可用的枚举值";
}
