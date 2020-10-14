package com.example.cardchain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public boolean emailValidation(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public String passwordStrength(String password){

        String suggestion = "";
        String final_suggestion = "";
        // Create the patterns to search digit,
        // upper case alphabet, lower case
        // alphabet and special character
        if (password.length() < 10){
            return "Password is weak. Add more characters";
        }

        Pattern digit = Pattern.compile("(\\d)");
        Pattern upper = Pattern.compile("([A-Z])");
        Pattern lower = Pattern.compile("([a-z])");
        Pattern spChar = Pattern.compile("(\\W)");

        // Search the above patterns in password.
        Matcher Digit = digit.matcher(password);
        Matcher Upper = upper.matcher(password);
        Matcher Lower = lower.matcher(password);
        Matcher Special = spChar.matcher(password);

        // If no digits are present
        if (!Digit.find()) {
            suggestion += ", digits";
        }
        // If no upper case alphabet is present
        if (!Upper.find()) {
            suggestion += ", uppercase characters";
        }
        // If no lower case alphabet is present
        if (!Lower.find()) {
            suggestion += ", lowercase characters";
        }
        // If no special character is is present
        if (!Special.find()) {
            suggestion += ", special characters";
        }

        if(suggestion.equals("") == false){
            String pre_suggest = "Password doesn't contain ";
            final_suggestion = pre_suggest + suggestion;
        }


        return final_suggestion;
    }
}
