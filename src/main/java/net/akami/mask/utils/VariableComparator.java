package net.akami.mask.utils;

import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.SingleCharVariable;
import net.akami.mask.expression.Variable;

import java.util.Comparator;

public class VariableComparator implements Comparator<Variable> {

    public static final VariableComparator COMPARATOR = new VariableComparator();

    private VariableComparator() {}

    @Override
    public int compare(Variable o1, Variable o2) {
        if(o1 instanceof SingleCharVariable && o2 instanceof SingleCharVariable)
            return compare((SingleCharVariable) o1, (SingleCharVariable) o2);

        if(o1 instanceof SingleCharVariable) {
            return -compare((SingleCharVariable) o1, (ComplexVariable) o2);
        }

        if(o2 instanceof SingleCharVariable) {
            return compare((SingleCharVariable) o2, (ComplexVariable) o1);
        }

        return compare((ComplexVariable) o1, (ComplexVariable) o2);
    }

    private int compare(SingleCharVariable s1, SingleCharVariable s2) {
        return s1.getVar() - s2.getVar();
    }

    private int compare(SingleCharVariable s1, ComplexVariable c1) {
        float sExponent = 1;
        float cExponent = c1.getFinalExponent().orElse(1f);
        return Float.compare(sExponent, cExponent);
    }

    private int compare(ComplexVariable c1, ComplexVariable c2) {
        float c1Exponent = c2.getFinalExponent().orElse(1f);
        float c2Exponent = c1.getFinalExponent().orElse(1f);
        return Float.compare(c1Exponent, c2Exponent);
    }
}
