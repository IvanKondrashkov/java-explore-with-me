package ru.practicum.validation;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IpAddressValidator.class)
@Documented
public @interface IpAddress {
    String message() default "Incorrect ip address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
