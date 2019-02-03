package net.akami.mask.utils;

import java.text.DecimalFormat;

public class MathUtils {

    public static float sum(float a, float b) {
        return a+b;
    }
    public static float subtract(float a, float b) {
        int length = Math.max((""+a).length(), (""+b).length());
        String pattern = "0.";
        for(int i = 2; i<length; i++)
            pattern += "#";
        DecimalFormat format = new DecimalFormat(pattern);

        return Float.parseFloat(format.format(a-b));
    }
    public static float mult(float a, float b) {
        return a*b;
    }
    public static float divide(float a, float b) {
        int length = Math.max((""+a).length(), (""+b).length());
        String pattern = "0.";
        for(int i = 2; i<length; i++)
            pattern += "#";
        System.out.println(pattern);
        DecimalFormat format = new DecimalFormat(pattern);

        return Float.parseFloat(format.format(a/b));
    }
    public static float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }
}
