package net.akami.mask.check;

public interface ValidityCheck {

    boolean isValid(String input);
    String errorMessage(String input);
}
