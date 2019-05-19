package net.akami.mask.exception;

import net.akami.mask.core.MaskExpression;

/**
 * MaskException is the common runtime exception handled by the library. It is thrown when a MaskExpression is
 * mathematically invalid, such as "3+" or "--4**^4" or "?4+4"
 *
 * @author Antoine Tran
 */
public class MaskException extends RuntimeException {

    private final MaskExpression origin;

    /**
     * @param message the message printed
     * @param origin the mask that threw the exception
     */
    public MaskException(String message, MaskExpression origin) {
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
