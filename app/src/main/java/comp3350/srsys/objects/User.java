package comp3350.srsys.objects;

public class User {

    private String email;
    private final String firstName;
    private String lastName;
    private String password;


    public User(String newEmail, String firstName, String lastName, String password) {
        email = newEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailInput) {
        this.email = emailInput;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newFirst) {
        lastName = newFirst;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newLast) {
        lastName = newLast;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }


    /* Equality is dependent on emails being the same as that's the primary key for Users */
    @Override
    public boolean equals(Object obj) {
        boolean areEmailsEqual = false;
        if (obj instanceof User) {
            areEmailsEqual = ((User) obj).getEmail().equals(this.email);
        }
        return areEmailsEqual;
    }

    @Override
    public User clone() {
        return new User(email, firstName, lastName, password);
    }
}