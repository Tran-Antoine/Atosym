package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationCache;
import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.display.visitor.DisplayerVisitor;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.operator.BinaryOperator;
import net.akami.atosym.utils.ParserUtils;

import java.util.Scanner;

public class MainTester {

    public static void main(String... args) {
        MaskContext context = new MaskContext();
        context.addDuplicatedCanceller(CalculationCache.supply(300), BinaryOperator.class);

        Scanner sc = new Scanner(System.in);
        String expression;

        System.out.println("Next expression to reduce : ");
        while(!(expression = sc.nextLine()).isEmpty()) {
            simplify(expression, context);
        }
    }

    private static void simplify(String expression, MaskContext context) {
        long time = System.nanoTime();
        MathObject result = ParserUtils.generateSimpleTree(expression, context).merge();
        DisplayerVisitor displayer = result.getDisplayer();
        System.out.println("Result (Infix notation)      : "+ displayer.accept(InfixNotationDisplayable.EMPTY_INSTANCE));
        //System.out.println("Result (Functional notation) : "+displayer.accept(FunctionalNotationDisplayable.EMPTY_INSTANCE));

        float deltaTime = (System.nanoTime() - time) / 1000000000f;
        System.out.println("Calculations ended after "+deltaTime+" seconds");
        System.out.println("Next expression to reduce : ");
    }
}
