package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.handler.sign.BinaryOperationSign;

import java.util.Objects;

public class FastAtosymMath {

    public static final BinaryOperationSign[] BINARY_OPERATIONS;

    static {
        BINARY_OPERATIONS = new BinaryOperationSign[]{
                BinaryOperationSign.SUM, BinaryOperationSign.SUBTRACT,
                BinaryOperationSign.MULT, BinaryOperationSign.DIVIDE,
                BinaryOperationSign.POW
        };
    }

    public static String reduce(String exp) {
        return reduce(exp, MaskContext.DEFAULT);
    }

    public static String reduce(String exp, MaskContext context) {
        Objects.requireNonNull(exp, "Cannot reduce a null expression");
        return ParserUtils.generateSimpleTree(exp, context)
                .merge()
                .getDisplayer()
                .accept(InfixNotationDisplayable.EMPTY_INSTANCE);
    }
}
