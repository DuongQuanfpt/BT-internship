package finalproject.group1.BE.commons;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public final class ValidateCommons {

    public static boolean isValidEmail(String email){
       return  Pattern.compile(Constants.VALID_EMAIL_PATERN).matcher(email).matches();
    }

    public static boolean isUserEmailValid(String email){
        System.out.println("validating email :");
        if(!ValidateCommons.isValidEmail(email)){
            return false;
        }
        return email.length() <= 255;
    }

    public static boolean isUserPasswordValid(String password){
        System.out.println("validating password :");
        return ValidateCommons.hasUpperCaseAndNumber(password) &&
                password.length() >= 8 && password.length() <= 32;
    }

    public static boolean isUserNameValid(String userName){
        System.out.println("validating username :");
        return userName.length() >= 8 && userName.length() <= 255;
    }

    public static boolean isValidDate (String date){
        System.out.println("validating date:");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);
            LocalDate startDate = LocalDate.parse(date, formatter);

        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean hasUpperCaseAndNumber(String text){
        char currentChar;
        boolean uppercaseFlag = false;
        boolean numberFlag = false;

        if(text == null){
            return false;
        }

        for (int i = 0; i < text.length(); i++) {
            currentChar = text.charAt(i);
            //check if current character is a number
            if (Character.isDigit(currentChar)) {
                numberFlag = true;
            }
            //check if the current character is uppercase
            if (Character.isUpperCase(currentChar)) {
                uppercaseFlag = true;
            }
        }

        return uppercaseFlag && numberFlag;
    }
}
