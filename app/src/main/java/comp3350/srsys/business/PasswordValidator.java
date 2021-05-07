package comp3350.srsys.business;

public class PasswordValidator implements IValidator {

    private String passwordRegex;

    public PasswordValidator() {
        this.passwordRegex = "^" +
                "(?=.*[0-9])" + //at least 1 digit
                "(?=.*[a-z])" + //at least 1 lower case letter
                "(?=.*[A-Z])" + //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" + //any letter
                "(?=.*[@#$%^&+=!/_'])" + //at least 1 special character
                "(?=\\S+$)" + //no white spaces
                ".{6,32}" + //at least 6 characters and not above 32
                "$";
    }

    @Override
    public boolean validate(Object password) {
        boolean isValid = false;
        if (password instanceof String)
            isValid = ((String) password).matches(passwordRegex);

        return isValid;
    }
}
