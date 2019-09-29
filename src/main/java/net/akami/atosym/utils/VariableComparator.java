package net.akami.atosym.utils;

import net.akami.atosym.expression.VariableExpression;

import java.util.Comparator;

public class VariableComparator implements Comparator<VariableExpression> {

    public static final VariableComparator COMPARATOR = new VariableComparator();

    private VariableComparator() {}

    @Override
    public int compare(VariableExpression o1, VariableExpression o2) {
        /*float o1Exponent = o1.getFinalExponent().orElse(1f);
        float o2Exponent = o2.getFinalExponent().orElse(1f);

        int comparison = Float.compare(o2Exponent, o1Exponent);
        if(comparison != 0) return comparison;

        return o1.getVar() - o2.getVar();*/
        return 0;
    }

}
