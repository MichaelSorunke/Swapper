package comp3350.srsys.business;

public class EmailValidator implements IValidator {

    private String emailRegex;

    public EmailValidator() {
        this.emailRegex = "^" +
                "[a-zA-Z0-9_.+-]" +
                "+@" +
                "[a-zA-Z0-9-]" +
                "+\\." +
                "[a-zA-Z0-9-.]" +
                "+$";
    }

    @Override
    public boolean validate(Object email) {
        boolean isValid = false;
        if (email instanceof String)
            isValid = ((String) email).matches(emailRegex);

        return isValid;
    }
}
