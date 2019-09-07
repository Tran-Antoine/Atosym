package net.akami.atosym.exception;

import net.akami.atosym.core.Mask;

/**
 * The common runtime exception handled by the library. It is thrown when a Mask is
 * mathematically invalid, such as "3+" or "--4**^4" or "?4+4"
 *
 * @author Antoine Tran
 */
public class MaskException extends RuntimeException {

    private final Mask origin;

    /**
     * @param message the message printed
     * @param origin the mask that threw the exception
     */
    public MaskException(String message, Mask origin) {
        super(message);
        this.origin = origin;
    }

    @Override
    public String getMessage() {
        return super.getMessage()+": "+origin;
    }

    @Override
    public void printStackTrace() {
        System.out.println(getMessage()+": "+origin);
        super.printStackTrace();
    }
}
