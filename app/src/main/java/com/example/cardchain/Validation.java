package com.example.cardchain;

import android.content.res.Resources;
import android.widget.ArrayAdapter;

import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
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

    public ArrayList<Integer> passwordStrength(String password){

        ArrayList<Integer> suggestion = new ArrayList<>();
        ArrayList<Integer> final_suggestion = new ArrayList<>();
        // Create the patterns to search digit,
        // upper case alphabet, lower case
        // alphabet and special character
        if (password.length() < 10){
            final_suggestion.add(R.string.pass_weak);
            return final_suggestion;
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
            suggestion.add(R.string.pass_dig);
        }
        // If no upper case alphabet is present
        if (!Upper.find()) {
            suggestion.add(R.string.pass_up);
        }
        // If no lower case alphabet is present
        if (!Lower.find()) {
            suggestion.add(R.string.pass_low);
        }
        // If no special character is is present
        if (!Special.find()) {
            suggestion.add(R.string.pass_spl);
        }

        if(suggestion.size() != 0){
            final_suggestion.add(R.string.pass_not_contain);
            final_suggestion.addAll(suggestion);
        }


        return final_suggestion;
    }

    public boolean phoneNumValidation(String phone_num){
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phone_num);

        return matcher.matches();
    }
}
