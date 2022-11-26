package beertag.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class ValidMinAgeStringImpl implements ConstraintValidator<MinAge, String> {

    private long minValue;

    @Override
    public void initialize(MinAge minValue) {
        this.minValue = minValue.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return LocalDate.now().minusYears(minValue).isAfter(LocalDate.parse(value));
    }
}
