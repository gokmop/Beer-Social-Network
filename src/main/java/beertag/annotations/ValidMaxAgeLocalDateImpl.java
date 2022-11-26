package beertag.annotations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ValidMaxAgeLocalDateImpl implements ConstraintValidator<MaxAge, LocalDate> {

    private long maxValue;

    @Override
    public void initialize(MaxAge maxValue) {
        this.maxValue = maxValue.value();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return LocalDate.now().minusYears(maxValue).isBefore(value);

    }
}
