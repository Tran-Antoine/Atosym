package net.akami.atosym.utils;

import java.util.Comparator;

public class VariableComparator implements Comparator<Variable> {

    public static final VariableComparator COMPARATOR = new VariableComparator();

    private VariableComparator() {}

    @Override
    public int compare(Variable o1, Variable o2) {
        float o1Exponent = o1.getFinalExponent().orElse(1f);
        float o2Exponent = o2.getFinalExponent().orElse(1f);

        int comparison = Float.compare(o2Exponent, o1Exponent);
        if(comparison != 0) return comparison;

        return o1.getVar() - o2.getVar();
    }

}
