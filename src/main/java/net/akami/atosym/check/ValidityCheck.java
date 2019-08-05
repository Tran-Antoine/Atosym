package net.akami.atosym.check;

/**
 * Verifies that a given string is a correct mathematical expression. <br>
 * Checks are performed at atosym reload time (whenever an expression is set).
 */
public interface ValidityCheck {

    /**
     * @param input the given input
     * @return whether the input is valid or not
     */
    boolean isValid(String input);

    /**
     * @param input the given input
     * @return the message error to throw, assuming {@link #isValid(String)} returns {@code false}.
     */
    String errorMessage(String input);
}
