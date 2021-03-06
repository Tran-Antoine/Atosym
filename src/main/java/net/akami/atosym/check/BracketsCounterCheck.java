package net.akami.atosym.check;

/**
 * Verifies that the number of opening parentheses {@code '('} is the same as the number of closing parentheses {@code ')'}
 */
public class BracketsCounterCheck implements ValidityCheck {

    @Override
    public boolean isValid(String input) {
        int openingLength = input.replaceAll("[^(]", "").length();
        int closingLength = input.replaceAll("[^)]", "").length();
        return openingLength == closingLength;
    }

    @Override
    public String errorMessage(String input) {
        return "Found non-closed or non-opened parenthesis";
    }
}
