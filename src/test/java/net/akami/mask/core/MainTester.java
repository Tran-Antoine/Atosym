package net.akami.mask.core;

import net.akami.mask.handler.PowCalculator;
import net.akami.mask.operation.MaskExpression;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.ReducerFactory;

import java.util.Scanner;

public class MainTester {

    public static void main(String... args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Input : ");
        String[] input = sc.nextLine().split(" ");
        long time = System.nanoTime();
        String r = MathUtils.pow(input[0], input[1]);
        System.out.println("Result with base method : "+r);
        System.out.println("Time : "+(System.nanoTime() - time) / 1000000000f+"s");
        long time2 = System.nanoTime();
        r = PowCalculator.getInstance().fastOperate(input[0], input[1]);
        System.out.println("Result with fast method : "+r);
        System.out.println("Time : "+(System.nanoTime() - time2) / 1000000000f+"s");

        String expression;
        MaskExpression.TEMP.reload(sc.nextLine());
        //System.out.println(MaskHandler.begin(MaskExpression.TEMP).differentiate('x').asExpression());

        System.out.println("Next expression to reduce : ");
        while(!(expression = sc.nextLine()).isEmpty()) {
            time = System.nanoTime();
            System.out.println("Result : "+ ReducerFactory.reduce(expression));
            float deltaTime = (System.nanoTime() - time2) / 1000000000f;
            System.out.println("Calculations ended after "+deltaTime+" seconds");
            System.out.println("Next expression to reduce : ");
        }
    }
}
