package net.akami.atosym.samples;

import net.akami.atosym.alteration.DegreeUnit;
import net.akami.atosym.core.Mask;
import net.akami.atosym.core.MaskContext;
import net.akami.atosym.core.MaskImageCalculator;
import net.akami.atosym.core.MaskOperatorHandler;
import net.akami.atosym.function.AngleUnitDependent;

import java.util.HashMap;
import java.util.Map;

public class MaskImageCalculatorSample {

    public static void main(String[] args) {

        Mask expression = new Mask("sin(x)^2+cos(y)^2");
        MaskContext context = new MaskContext();
        context.addGlobalModifier(new DegreeUnit(), AngleUnitDependent.class);

        MaskOperatorHandler handler = new MaskOperatorHandler(context);

        Map<Character, String> values = new HashMap<>();
        values.put('x', "90");
        String image0 = handler
                .compute(MaskImageCalculator.class, expression, Mask.TEMP, values)
                .asExpression();

        values.put('y', "0");
        String image1 = handler
                .compute(MaskImageCalculator.class, expression, Mask.TEMP, values)
                .asExpression();

        System.out.println(image0);
        System.out.println(image1);

        /*
            Expected results :

            cos(y)^2.0+1.0
            2.0
         */
    }
}
