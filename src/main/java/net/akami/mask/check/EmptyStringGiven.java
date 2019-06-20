package net.akami.mask.check;

/**
 * Verifies that the given input is not empty.
 */
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
