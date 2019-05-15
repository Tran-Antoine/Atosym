package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.*;

public class Variable implements Comparable<Variable> {

    private final char var;
    private final Expression exponent;
    private final MathFunction function;
    private final String expression;
    private final MaskContext context;

    public Variable(char var, MaskContext context) {
        this(var, null, context);
    }

    public Variable(char var, Expression exponent, MaskContext context) {
        this(var, exponent, null, context);
    }

    public Variable(char var, Expression exponent, MathFunction function, MaskContext context) {
        this.var = var;
        this.exponent = exponent == null ? Expression.of(1) : exponent;
        this.function = function;
        this.expression = loadExpression();
        this.context = context == null ? MaskContext.DEFAULT : context;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Variable)) return false;

        Variable other = (Variable) obj;

        return var == other.var && exponent.equals(other.exponent);
    }

    public static Variable[] combine(Variable[] a1, Variable[] a2) {
        if(a1 == null) a1 = new Variable[0];
        if(a2 == null) a2 = new Variable[0];
        List<Variable> finalVars = MergeManager.merge(Arrays.asList(a1), Arrays.asList(a2), Variable.class);
        Collections.sort(finalVars);
        return finalVars.toArray(new Variable[0]);
    }

    public static List<Variable> dissociate(Variable[] a) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable current : a) {
            if(current.exponent != null && ExpressionUtils.isANumber(current.exponent)) {
                float expValue = ((Monomial) current.exponent.get(0)).getNumericValue();

                while (expValue > 1) {
                    finalVars.add(new Variable(current.var, current.context));
                    expValue--;
                }

                if (expValue != 0)
                    finalVars.add(new Variable(current.var, Expression.of(expValue), current.context));
            } else {
                finalVars.add(current);
            }
        }
        return finalVars;
    }

    private String loadExpression() {
        String result = exponent == null || exponent.toString().equals("1.0") || exponent.toString().equals("1")
                ? String.valueOf(var) : var + "^" + exponent.toString();
        return result;
    }

    public String getExpression() {
        return expression;
    }

    public char getVar() {
        return var;
    }

    public MathFunction getFunction() {
        return function;
    }

    public MaskContext getContext() {
        return context;
    }

    public Expression getExponent() {
        return exponent;
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public int compareTo(Variable o) {
        return this.var - o.var;
    }


}
