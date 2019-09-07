package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationCache;
import net.akami.atosym.alteration.DegreeUnit;
import net.akami.atosym.alteration.PowExpansionLimit;
import net.akami.atosym.function.AngleUnitDependent;
import net.akami.atosym.handler.BinaryOperationHandler;
import net.akami.atosym.handler.PowerCalculator;

public class MaskDemo {

    public static void main(String[] args) {

        // Creates 3 objects handling various expressions
        Mask mask1 = new Mask("cos(90) - a/b/c/d");
        Mask mask2 = new Mask("(a+b)^100.0 + (a+b)^3");
        Mask mask3 = new Mask("(3-x)^3 + 3x - 12x^2 - 2x^2");

        // Initializing a calculation environment to customize the calculations
        MaskContext context = new MaskContext();
        // Alteration #1 : Changes angles to degrees
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);
        // Alteration #2 : Stops (...)^x expansions when x is over 50
        context.addGlobalCanceller(new PowExpansionLimit(50, context), PowerCalculator.class);
        // Alteration #3 : Adds a cache that store the results from the previous calculations to increase the performances
        // This alteration must be duplicated, which means that every object computing results must have its own cache
        context.addDuplicatedCanceller(CalculationCache::new, BinaryOperationHandler.class);

        MaskOperatorHandler manager = new MaskOperatorHandler(context);

        // Stores the simplified result to the temporary Mask instance, in order to get its string value
        String result1 = manager
                .compute(MaskSimplifier.class, mask1, Mask.TEMP, null)
                .asExpression();
        String result2 = manager
                // "begin(Mask)" is an alternative to putting (in this case) mask2 before "Mask.TEMP" as in the previous example
                .begin(mask2)
                .compute(MaskSimplifier.class, Mask.TEMP, null)
                .asExpression();

        // Instead of retrieving the string value, we want to store the result of the calculation in a different object
        Mask result3 = new Mask();
        manager
                .begin(mask3)
                // instead of using Mask.TEMP, we change the mask itself so its new string value is simplified
                .compute(MaskSimplifier.class, mask3, null)
                // we store the derivative in another object
                .compute(MaskDerivativeCalculator.class, result3, 'x');

        System.out
                .printf("%s = %s\n", mask1.getExpression(), result1)
                .printf("%s = %s\n", mask2.getExpression(), result2)
                .printf("(%s)' = %s", mask3.getExpression(), result3.getExpression());
    }
}
