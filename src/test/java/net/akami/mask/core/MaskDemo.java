package net.akami.mask.core;

import net.akami.mask.alteration.DegreeUnit;
import net.akami.mask.alteration.PowExpansionLimit;
import net.akami.mask.function.AngleUnitDependent;
import net.akami.mask.handler.PowerCalculator;

public class MaskDemo {

    public static void main(String[] args) {

        Mask trigoExpression = new Mask("cos(90)");
        Mask binomialExpression = new Mask("(a+b)^5.0");
        MaskContext context = new MaskContext();
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);
        context.addGlobalCanceller(new PowExpansionLimit(3, context), PowerCalculator.class);

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
                (a+b)^5.0 = (a+b)^5.0
         */
    }
}
