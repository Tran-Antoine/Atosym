package net.akami.mask.check;

/**
 * Verifies that the given input does not contain any non-manageable character. <p>
 * Only letters, numbers and mathematical symbols are allowed
 */
public class UnknownCharacter implements ValidityCheck {

    private static final String ALLOWED_CHARS = "[a-zA-Z0-9.()+\\-*/^]+";
    @Override
    public boolean isValid(String input) {
        return input.matches(ALLOWED_CHARS);
    }

    @Override
    public String errorMessage(String input) {
        return "Found unmanageable character(s) : "+input.replaceAll(ALLOWED_CHARS, "");
    }
}
