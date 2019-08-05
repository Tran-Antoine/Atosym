package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationCache;
import net.akami.atosym.handler.BinaryOperationHandler;
import net.akami.atosym.utils.ReducerFactory;

import java.util.Scanner;

public class MainTester {

    public static void main(String... args) {

        MaskContext context = new MaskContext();
        // Adds a cache for every handler supported, including binary operators and math functions
        context.addClonedCanceller(CalculationCache.supply(300), BinaryOperationHandler.class);

        Scanner sc = new Scanner(System.in);
        String expression;

        System.out.println("Next expression to reduce : ");
        while(!(expression = sc.nextLine()).isEmpty()) {
            long time = System.nanoTime();
            System.out.println("Result : "+ ReducerFactory.reduce(expression, context));
            float deltaTime = (System.nanoTime() - time) / 1000000000f;
            System.out.println("Calculations ended after "+deltaTime+" seconds");
            System.out.println("Next expression to reduce : ");
        }
    }
}
