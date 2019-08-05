package net.akami.atosym.samples;

import net.akami.atosym.core.Mask;
import net.akami.atosym.core.MaskDerivativeCalculator;
import net.akami.atosym.core.MaskOperatorHandler;

public class MaskDerivativeCalculatorSample {

    public static void main(String[] args) {

        Mask expression = new Mask("(3x^2 + 5x^3) / (2x + x^2)");
        Mask derivative = new Mask();

        MaskOperatorHandler handler = MaskOperatorHandler.DEFAULT;

        handler.compute(MaskDerivativeCalculator.class, expression, derivative, 'x');

        System.out.println(derivative.getExpression());

        // Expected result : (5.0x^4.0+20.0x^3.0+6.0x^2.0)/(x^4.0+4.0x^3.0+4.0x^2.0)
        // Although it is a 100% correct solution, it is not the most reduced result, since polynomial division has not been implemented yet.
    }
}
