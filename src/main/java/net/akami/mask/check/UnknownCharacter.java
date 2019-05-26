package net.akami.mask.check;

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
