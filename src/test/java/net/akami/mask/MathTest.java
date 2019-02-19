package net.akami.mask;

import net.akami.mask.utils.ReducerFactory;

public class MathTest {

    public static void main(String... args) {

        System.out.println(ReducerFactory.reduce("((1+2)*3)*4"));
        System.out.println(ReducerFactory.reduce("(5x+4x)*1y"));
    }
}
