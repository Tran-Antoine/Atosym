package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;
import net.akami.mask.handler.Adder;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.Mergeable;

import java.util.*;

public class Variable implements Comparable<Variable>, Mergeable<Variable> {

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
        this.exponent = exponent == null ? new Expression(1) : exponent;
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
        List<Variable> finalVars = Mergeable.merge(Arrays.asList(a1), Arrays.asList(a2));
        Collections.sort(finalVars);
        return finalVars.toArray(new Variable[0]);
    }

    private String loadExpression() {
        String result = exponent == null || exponent.toString().equals("1.0") || exponent.toString().equals("1")
                ? String.valueOf(var) : var + "^" + exponent.toString();
        return result;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public int compareTo(Variable o) {
        return this.var - o.var;
    }

    @Override
    public boolean isMergeable(Variable other) {

        boolean equalVars = var == other.var;
        boolean noFunction = function == other.function;

        if(!equalVars) return false;
        if(noFunction) return true;

        if(function == null || other.function == null)
            return false;

        return function.equals(other.function);
    }

    @Override
    public Variable mergeElement(Variable other) {
        Adder operator = context.getBinaryOperation(Adder.class);

        Expression newExponent = operator.simpleSum(this.exponent, other.exponent);
        return new Variable(this.var, newExponent, this.function, this.context);
    }
}
