package net.akami.mask.core;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.operation.MaskOperator;
import net.akami.mask.utils.ReducerFactory;

import java.util.Scanner;

public class MainTester {

    public static void main(String... args) {

        Scanner sc = new Scanner(System.in);
        String expression;
        MaskExpression.TEMP.reload(sc.nextLine());
        System.out.println(MaskOperator.begin(MaskExpression.TEMP).differentiate('x').asExpression());

        System.out.println("Next expression to reduce : ");
        while(!(expression = sc.nextLine()).isEmpty()) {
            long time = System.nanoTime();
            System.out.println("Result : "+ ReducerFactory.reduce(expression));
            float deltaTime = (System.nanoTime() - time) / 1000000000f;
            System.out.println("Calculations ended after "+deltaTime+" seconds");
            System.out.println("Next expression to reduce : ");
        }
    }
}
