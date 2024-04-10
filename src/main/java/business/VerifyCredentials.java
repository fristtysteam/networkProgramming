package business;

public class VerifyCredentials {
    public static boolean checkPassword(String password) {
        String passwordRegExpression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*/]).{8,20}$";
        return password.matches(passwordRegExpression);
    }

}
