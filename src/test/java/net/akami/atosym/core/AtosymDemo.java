package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationCache;
import net.akami.atosym.alteration.DegreeUnit;
import net.akami.atosym.alteration.PowExpansionLimit;
import net.akami.atosym.function.AngleUnitDependent;
import net.akami.atosym.handler.BinaryOperationHandler;
import net.akami.atosym.handler.PowerCalculator;

public class AtosymDemo {

    public static void main(String[] args) {

        // Creates 3 objects handling various expressions
        Mask mask1 = new Mask("cos(90) - a/b/c/d");
        Mask mask2 = new Mask("(a+b)^100.0 + (a+b)^3");
        Mask mask3 = new Mask("(3-x)^3 + 3x - 12x^2 - 2x^2");

        MaskOperatorHandler handler = generateHandler();
        sample1(mask1, handler);
        sample2(mask2, handler);
        sample3(mask3, handler);
    }

    private static MaskOperatorHandler generateHandler() {
        // Initializing a calculation environment to customize the calculations
        MaskContext context = new MaskContext();
        // Alteration #1 : Changes angles to degrees
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);
        // Alteration #2 : Stops (...)^x expansions when x is over 10
        context.addGlobalCanceller(new PowExpansionLimit(10, context), PowerCalculator.class);
        // Alteration #3 : Adds a cache that store the results from the previous calculations to increase the performances
        // This alteration must be duplicated, which means that every object computing results must have its own cache
        context.addClonedCanceller(CalculationCache::new, BinaryOperationHandler.class);
        return new MaskOperatorHandler(context);
    }

    private static void sample1(Mask input, MaskOperatorHandler handler) {
        // Stores the simplified result to the temporary Mask instance, in order to get its string value
        String result1 = handler
                .compute(MaskSimplifier.class, input, Mask.TEMP, null)
                .asExpression();
        // cos(90) indeed gives 0 since we set the angle unit to degrees
        System.out.printf("%s = %s\n", input.getExpression(), result1);
    }

    private static void sample2(Mask input, MaskOperatorHandler handler) {
        String result2 = handler
                // "begin(Mask)" is an alternative to putting (in this case) mask2 before "Mask.TEMP" as in the previous example
                .begin(input)
                .compute(MaskSimplifier.class, Mask.TEMP, null)
                .asExpression();
        // (a+b)^100 is not expanded, whereas (a+b)^3 is, since we set the expansion limit to 10
        System.out.printf("%s = %s\n", input.getExpression(), result2);
    }

    private static void sample3(Mask input, MaskOperatorHandler handler) {
        // Instead of retrieving the string value, we want to store the result of the calculation in a different object
        Mask result3 = new Mask();
        handler
                .begin(input)
                // instead of using Mask.TEMP, we change the mask itself so its new string value is simplified
                .compute(MaskSimplifier.class, input, null)
                // we store the derivative in another object
                .compute(MaskDerivativeCalculator.class, result3, 'x');
        // The left expression is simplified, since we changed the value of "mask3" when we simplified the expression
        System.out.printf("(%s)' = %s", input.getExpression(), result3.getExpression());
    }
}
