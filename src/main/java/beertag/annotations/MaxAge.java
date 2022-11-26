package beertag.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidMaxAgeLocalDateImpl.class, ValidMaxAgeStringImpl.class})
public @interface MaxAge {

    long value();

    String message() default "{beertag.annotations.MaxAge.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
