package net.akami.mask.check;

/**
 * Verifies that the first character of the given input is valid
 */
public class IllegalStartingCharacter implements ValidityCheck {

    @Override
    public boolean isValid(String input) {
        return !".*/^)".contains(String.valueOf(input.charAt(0)));
    }

    @Override
    public String errorMessage(String input) {
        return "Invalid starting character found : " + input.charAt(0);
    }
}
