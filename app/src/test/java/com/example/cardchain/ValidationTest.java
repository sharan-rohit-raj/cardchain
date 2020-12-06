package com.example.cardchain;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class ValidationTest extends TestCase {
    @Test
    public void testEmailValidation() {
        String[] test_inputs = {"", "abc", "@abc", "@abc.com", "abc@def.com"};

        Validation validation = new Validation();
        boolean[] test_outputs = new boolean[test_inputs.length];
        boolean[] expected_test_out = {false, false, false, false, true};
        for(int test_input_index = 0; test_input_index < test_inputs.length; test_input_index ++){
            test_outputs[test_input_index] = validation.emailValidation(test_inputs[test_input_index]);
        }

        for(int bool_ind = 0; bool_ind < test_outputs.length; bool_ind ++){
            assertEquals(expected_test_out[bool_ind], test_outputs[bool_ind]);
        }
    }

    @Test
    public void testPasswordStrength() {
        String[] test_inputs = {"", "abc", "abcdefghigjkl", "1234567890", "a1234567980", "aA1234567890", "aA~1234567890"};
        ArrayList<ArrayList<Integer>> test_outputs= new ArrayList<>();

        ArrayList<Integer> output1 = new ArrayList<>();
        output1.add(R.string.pass_weak);

        ArrayList<Integer> output2 = new ArrayList<>();
        output2.add(R.string.pass_weak);

        ArrayList<Integer> output3 = new ArrayList<>();
        output3.add(R.string.pass_not_contain);
        output3.add(R.string.pass_dig);
        output3.add(R.string.pass_up);
        output3.add(R.string.pass_spl);

        ArrayList<Integer> output4 = new ArrayList<>();
        output4.add(R.string.pass_not_contain);
        output4.add(R.string.pass_up);
        output4.add(R.string.pass_low);
        output4.add(R.string.pass_spl);

        ArrayList<Integer> output5 = new ArrayList<>();
        output5.add(R.string.pass_not_contain);
        output5.add(R.string.pass_up);
        output5.add(R.string.pass_spl);

        ArrayList<Integer> output6 = new ArrayList<>();
        output6.add(R.string.pass_not_contain);
        output6.add(R.string.pass_spl);

        ArrayList<Integer> output7 = new ArrayList<>();

        test_outputs.add(output1);
        test_outputs.add(output2);
        test_outputs.add(output3);
        test_outputs.add(output4);
        test_outputs.add(output5);
        test_outputs.add(output6);
        test_outputs.add(output7);


        Validation validation = new Validation();
        ArrayList<ArrayList<Integer>> actual_test_out = new ArrayList<>();
        for(int i = 0; i< test_inputs.length; i++){
            actual_test_out.add(null);
        }
        for(int test_input_index = 0; test_input_index < test_inputs.length; test_input_index ++){
            actual_test_out.set(test_input_index, validation.passwordStrength(test_inputs[test_input_index]));
        }

        for(int expected_test_ind = 0; expected_test_ind < test_outputs.size(); expected_test_ind ++){
            for(int j = 0; j < actual_test_out.get(expected_test_ind).size(); j ++){
                assertEquals(test_outputs.get(expected_test_ind).get(j), actual_test_out.get(expected_test_ind).get(j));
            }
        }
    }

    @Test
    public void testPhoneNumValidation() {
        String[] test_inputs = {"", "123", "1234567890"};

        Validation validation = new Validation();
        boolean[] test_outputs = new boolean[test_inputs.length];
        boolean[] expected_test_out = {false, false, true};
        for(int test_input_index = 0; test_input_index < test_inputs.length; test_input_index ++){
            test_outputs[test_input_index] = validation.phoneNumValidation(test_inputs[test_input_index]);
        }

        for(int bool_ind = 0; bool_ind < test_outputs.length; bool_ind ++){
            assertEquals(expected_test_out[bool_ind], test_outputs[bool_ind]);
        }
    }
}