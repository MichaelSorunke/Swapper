package comp3350.srsys.business;

public class ValidatorService {
    private static IValidator passwordValidator = null;
    private static IValidator emailValidator = null;

    public static IValidator getPasswordValidator() {
        if (passwordValidator == null)
            passwordValidator = new PasswordValidator();
        return passwordValidator;
    }

    public static IValidator getEmailValidator() {
        if (emailValidator == null)
            emailValidator = new EmailValidator();
        return emailValidator;
    }
}
