package beertag.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class ValidMinAgeLocalDateImpl implements ConstraintValidator<MinAge, LocalDate> {

    private long minValue;

    @Override
    public void initialize(MinAge minValue) {
        this.minValue = minValue.value();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return LocalDate.now().minusYears(minValue).isAfter(value);
    }
}
