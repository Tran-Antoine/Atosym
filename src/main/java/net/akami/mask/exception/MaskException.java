package net.akami.mask.exception;

import net.akami.mask.math.MaskExpression;

public class MaskException extends RuntimeException {

    private final MaskExpression origin;

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
