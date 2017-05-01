package com.volunteers.anotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = IsEndDateBeforeStartDate.class)
public @interface EndDateBeforeStartDate {

    String message() default "should be after start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
