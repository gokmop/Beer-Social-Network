package beertag.annotations;

import beertag.enums.UserRoles;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidRoleImpl implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals(UserRoles.ROLE_ADMIN.name()) || value.equals(UserRoles.ROLE_USER.name());
    }
}
