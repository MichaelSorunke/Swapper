package comp3350.srsys.business;

public class UserValidator {
    private static IValidator passwordValidator = ValidatorService.getPasswordValidator();
    private static IValidator emailValidator = ValidatorService.getEmailValidator();

    public static boolean validateEmail(String email) {
        return emailValidator.validate(email);
    }

    public static boolean validatePassword(String password) {
        return passwordValidator.validate(password);
    }
}
