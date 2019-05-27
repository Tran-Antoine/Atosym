package net.akami.mask.check;

public class BracketsCounterCheck implements ValidityCheck {

    @Override
    public boolean isValid(String input) {
        int openingLength = input.replaceAll("[^(]", "").length();
        int closingLength = input.replaceAll("[^)]", "").length();
        return openingLength == closingLength;
    }

    @Override
    public String errorMessage(String input) {
        return null;
    }
}
