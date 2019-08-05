package net.akami.atosym.samples;

import net.akami.atosym.core.Mask;
import net.akami.atosym.core.MaskOperatorHandler;
import net.akami.atosym.core.MaskSimplifier;

public class MaskSimplifierSample {

    public static void main(String[] args) {

        Mask expression = new Mask("(a+b)^2 - 6a^2 + 6x/(3y)");
        MaskOperatorHandler handler = MaskOperatorHandler.DEFAULT;

        handler.compute(MaskSimplifier.class, expression, expression, null);
        System.out.println(expression.getExpression());


        // Expected output : -5.0a^2.0+b^2.0+2.0ab+(2.0x)/y
    }
}
