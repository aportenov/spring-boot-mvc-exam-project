package com.volunteers.anotations;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=EmailExistsConstraintValidator.class)
public @interface EmailExistsConstraint {

    String message() default "Username already exists";
    Class[] groups() default {};
    Class[] payload() default {};
}
