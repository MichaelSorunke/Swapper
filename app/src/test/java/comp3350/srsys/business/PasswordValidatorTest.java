package comp3350.srsys.business;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordValidatorTest {

    private static IValidator passwordValidator;

    @BeforeClass
    public static void classSetUp() {
        passwordValidator = ValidatorService.getPasswordValidator();
    }

    @Test
    public void validateValidPassword() {
        String password = "John123!@alj";

        boolean isValid = passwordValidator.validate(password);

        assertTrue(isValid);
    }

    @Test
    public void validateEmptyPassword() {
        String password = "";

        boolean isValid = passwordValidator.validate(password);

        assertFalse(isValid);
    }

    @Test
    public void validatePasswordMissingCapital() {
        String password = "john123!@alj";

        boolean isValid = passwordValidator.validate(password);

        assertFalse(isValid);
    }

    @Test
    public void validatePasswordMissingNumbers() {
        String password = "john!@alj";

        boolean isValid = passwordValidator.validate(password);

        assertFalse(isValid);
    }

    @Test
    public void validatePasswordWithoutSpecialCharacters() {
        String password = "Johnny1238ABC";

        boolean isValid = passwordValidator.validate(password);

        assertFalse(isValid);
    }
}