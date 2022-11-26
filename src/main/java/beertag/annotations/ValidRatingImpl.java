package beertag.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidRatingImpl implements ConstraintValidator<ValidRating, Integer> {
    private int maxRating;

    @Override
    public void initialize(ValidRating maxRating) {
        this.maxRating = maxRating.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value >= 0 && value <= maxRating;
    }
}
