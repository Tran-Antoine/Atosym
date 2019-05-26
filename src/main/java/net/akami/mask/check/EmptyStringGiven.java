package net.akami.mask.check;

public class EmptyStringGiven implements ValidityCheck {
    @Override
    public boolean isValid(String input) {
        return !input.isEmpty();
    }

    @Override
    public String errorMessage(String input) {
        return "Empty sequence cannot be converted to a mathematical expression";
    }
}
