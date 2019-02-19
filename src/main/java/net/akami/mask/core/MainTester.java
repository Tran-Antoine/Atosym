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
                .imageFor(next, 2)
                // Ends the operations with initial as the default mask
                .end();

        System.out.println("Initial transformed : " + initial);
        System.out.println("Next transformed : " + next);

        MaskExpression nonReducible = new MaskExpression("3x");

        // throws an exception, because 3x obviously cannot be converted to an integer
        System.out.println(MaskOperator.begin(nonReducible).asInt());
    }
}
