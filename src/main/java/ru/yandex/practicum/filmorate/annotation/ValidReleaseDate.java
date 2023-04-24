package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ValidReleaseDate {
    String message() default "Дата релиза - не раньше 28 декабря 1895 года!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
