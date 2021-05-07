package comp3350.srsys.business;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmailValidatorTest {

    private static IValidator emailValidator;

    @BeforeClass
    public static void setUpClass() {
        emailValidator = ValidatorService.getEmailValidator();
    }

    @Test
    public void validateEmailGeneralCase() {
        String email = "bob123.bobert@yahoo.com";

        boolean isValid = emailValidator.validate(email);

        assertTrue(isValid);
    }

    @Test
    public void validateEmailWithSpecialCharacters() {
        String email = "#HA&!)9~@bob.com";

        boolean isValid = emailValidator.validate(email);

        assertFalse(isValid);
    }

    @Test
    public void validateEmailWithSpecialCharactersAfterDot() {
        String email = "bob-bobert@yahoo.co#$%^&m";

        boolean isValid = emailValidator.validate(email);

        assertFalse(isValid);
    }

    @Test
    public void validateEmailWithSpecialCharactersAfterAt() {
        String email = "bob@yahoo!#$%.com";

        boolean isValid = emailValidator.validate(email);

        assertFalse(isValid);
    }

    @Test
    public void validateEmailWithoutAtSign() {
        String email = "bobyahoo.com";

        boolean isValid = emailValidator.validate(email);

        assertFalse(isValid);
    }

    @Test
    public void validateEmailWithoutDot() {
        String email = "bob@yahoocom";

        boolean isValid = emailValidator.validate(email);

        assertFalse(isValid);
    }
}