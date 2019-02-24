package net.akami.mask;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.ReducerFactory;

import java.util.Scanner;

public class MathTest {

    public static void main(String... args) {

        MaskExpression exp = new MaskExpression("3x^2 + 6xy + 3");
        System.out.println(MaskOperator.begin().differentiate(exp, MaskExpression.TEMP, 'x',true).asExpression());

        //System.out.println(MathUtils.sum("xx", "x^2"));
        //MaskExpression exp = new MaskExpression("5x + 3y");
        //System.out.println(MaskOperator.begin(exp).differentiate('y').asExpression());
        Scanner sc = new Scanner(System.in);
        String expression;

        System.out.println("Next expression to reduce : ");
        while(!(expression = sc.nextLine()).isEmpty()) {
            long time = System.nanoTime();
            System.out.println("Result : "+ReducerFactory.reduce(expression));

            float deltaTime = (System.nanoTime() - time) / 1000000000f;
            System.out.println("Calculations ended after "+deltaTime+" seconds");
            System.out.println("Next expression to reduce : ");
        }

        //System.out.println(MathUtils.sum("-168", "3"));
        //System.out.println(MathUtils.sum(Arrays.asList("5", "-4", "-x", "3y", "4x")));
        //System.out.println(MathUtils.subtract("5x", "3+x+2"));
        //System.out.println(ReducerFactory.reduce("5+2"));
        //System.out.println(ReducerFactory.reduce("3*((x+2y)*2 - 8z)"));
        //System.out.println(ReducerFactory.reduce("((1+2)*3)*4"));
        //System.out.println(ReducerFactory.reduce("(5x+4x)*7y"));
        //System.out.println(ReducerFactory.reduce("(5x+3y)*3"));
        //System.out.println(ReducerFactory.reduce("5x*y"));
        //for(int i = 0; i<5000; i++)
            //System.out.println(ReducerFactory.reduce("((2+7)^(23-19)-((3+9)*10))^3"));
        //System.out.println(MathUtils.mult("5x+3y+8z+9w", "2x+3y+12z+4w"));
    }
}
