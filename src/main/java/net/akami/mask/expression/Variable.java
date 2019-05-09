package net.akami.mask.expression;

import net.akami.mask.function.MathFunction;

import java.util.*;
import java.util.stream.Collectors;

public class Variable implements ExpressionElement, Comparable<Variable> {

    private char var;
    private Expression exponent;
    private MathFunction function;

    public Variable(char var, Expression exponent, MathFunction function) {
        this.var = var;
        this.exponent = exponent == null ? new Expression(1) : exponent;
        this.function = function;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Variable)) return false;

        Variable other = (Variable) obj;

        return var == other.var && exponent.equals(other.exponent);
    }

    public static Variable[] combine(Variable[] a1, Variable[] a2) {
        if(a1 == null) return a2;
        if(a2 == null) return a1;
        List<Variable> finalVars = new ArrayList<>();
        int i = 0;
        for(Variable var1 : a1) {
            int j = 0;
            for(Variable var2 : a2) {
                if (var2 != null) {
                    if (var1.var == var2.var && var1.function == var2.function) {
                        a1[i] = null;
                        a2[j] = null;
                        Expression newExponent = var1.exponent.simpleSum(var2.exponent);
                        finalVars.add(new Variable(var1.var, newExponent, var1.function));
                        break;
                    }
                }
                j++;
            }
            i++;
        }
        finalVars.addAll(Arrays.asList(a1).stream().filter(Objects::nonNull).collect(Collectors.toList()));
        finalVars.addAll(Arrays.asList(a2).stream().filter(Objects::nonNull).collect(Collectors.toList()));
        Collections.sort(finalVars);
        return finalVars.toArray(new Variable[finalVars.size()]);
    }

    @Override
    public String getExpression() {
        String result = exponent == null || exponent.toString().equals("1.0") || exponent.toString().equals("1")
                ? String.valueOf(var) : var + "^" + exponent.toString();
        return result;
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
