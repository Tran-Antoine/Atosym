package net.akami.mask.core;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;

public class MainTester {

    public static void main(String... args) {

        MaskExpression initial = new MaskExpression("4x - 5y + 3");
        MaskExpression next = new MaskExpression();

        System.out.println("Initial : " + initial);

        MaskOperator.begin(initial)
                // Changes the initial mask
                .imageFor(1)
                // Conserves the initial mask, writes the result of the operation in the next mask
                .imageFor(next, true, 2)
                // Ends the operations with initial as the default mask
                .end();

        System.out.println("Initial transformed : " + initial);
        System.out.println("Next transformed : " + next);

        MaskExpression curve = new MaskExpression("x^2 -3x + 5");
        MaskOperator operator = MaskOperator.begin(curve);

        for(int i = 0; i < 50; i++) {
            operator.imageFor(MaskExpression.TEMP, false, i);
            System.out.println(operator.asInt(MaskExpression.TEMP));
        }
        operator.end();

        /*
        Output :

            Initial : 4x-5y+3
            Initial transformed : 4-5y+3
            Next transformed : -3

            5
            3
            3
            5
            9
            15
            23
            33
            ...
         */
    }
}
