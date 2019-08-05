package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationCache;
import net.akami.atosym.alteration.DegreeUnit;
import net.akami.atosym.alteration.PowExpansionLimit;
import net.akami.atosym.function.AngleUnitDependent;
import net.akami.atosym.handler.BinaryOperationHandler;
import net.akami.atosym.handler.PowerCalculator;

public class MaskDemo {

    public static void main(String[] args) {

        Mask trigoExpression = new Mask("cos(90)");
        Mask binomialExpression = new Mask("(a+b)^100.0");
        MaskContext context = new MaskContext();
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);
        context.addGlobalCanceller(new PowExpansionLimit(50, context), PowerCalculator.class);
        context.addClonedCanceller(CalculationCache.supply(300), BinaryOperationHandler.class);

        MaskOperatorHandler manager = new MaskOperatorHandler(context);

        String simplifiedTrigo = manager
                .compute(MaskSimplifier.class, trigoExpression, Mask.TEMP, null)
                .asExpression();
        String simplifiedBinomial = manager
                .compute(MaskSimplifier.class, binomialExpression, Mask.TEMP, null)
                .asExpression();

        System.out.println(trigoExpression.getExpression() + " = " + simplifiedTrigo);
        System.out.println(binomialExpression.getExpression() + " = " + simplifiedBinomial);

        /*
            Output :

                cos(90) = 0
                (a+b)^5.0 = (a+b)^100.0
         */
    }
}
