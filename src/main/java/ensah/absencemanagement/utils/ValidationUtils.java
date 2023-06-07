package ensah.absencemanagement.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidationUtils {

    public static String getFirstError(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<Object>> violations = factory.getValidator().validate(object);
        if (!violations.isEmpty())
            return violations.stream().toList().get(0).getMessage();
        else
            return null;
    }

    public static boolean hasErrors(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<Object>> violations = factory.getValidator().validate(object);
        return !violations.isEmpty();
    }

}
