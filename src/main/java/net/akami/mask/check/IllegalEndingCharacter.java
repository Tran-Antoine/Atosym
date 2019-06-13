package net.akami.mask.check;

public class IllegalEndingCharacter implements ValidityCheck {

    @Override
    public boolean isValid(String input) {
        return !"+-.*/^(".contains(String.valueOf(input.charAt(input.length()-1)));
    }

    @Override
    public String errorMessage(String input) {
        return "Invalid ending character found : " + input.charAt(input.length()-1);
    }
}
