package net.akami.mask.expression;


import net.akami.mask.utils.ExpressionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExpressionElement implements Comparable<ExpressionElement> {

    private final String expression;
    private final float numericValue;
    private final List<Variable> variables;

    public ExpressionElement(float numericValue, Variable variable) {
        this(numericValue, Collections.singletonList(variable));
    }

    public ExpressionElement(float numericValue, List<Variable> variables) {
        this.numericValue = numericValue;
        this.variables = Objects.requireNonNull(Collections.unmodifiableList(variables));
        this.expression = loadExpression();
    }

    protected String loadExpression() {

        if(numericValue == 0) return "";
        if(variables.isEmpty()) return String.valueOf(numericValue);
        if(numericValue == 1)   return variablesToString();
        if(numericValue == -1)  return '-' + variablesToString();

        return numericValue + variablesToString();
    }

    public boolean hasSameVariablePartAs(ExpressionElement other) {

        if(this.variables.size() != other.variables.size()) return false;

        for(int i = 0; i < variables.size(); i++) {
            if(!this.variables.get(i).equals(other.variables.get(i)))
                return false;
        }
        return true;
    }

    public float getNumericValue() {
        return numericValue;
    }

    public String variablesToString() {
        if(variables.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();
        for(Variable var : variables)
            builder.append(var.getExpression());
        return builder.toString();
    }

    public boolean requiresBrackets() {
        int count = 0;
        if(Math.abs(numericValue) != 1) count++;
        if(variables.size() != 0)       count++;
        if(variables.size() > 1)        count++;
        if(expression.startsWith("(") && expression.endsWith(")")) count = 0;

        return count > 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ExpressionElement))
            return false;

        ExpressionElement other = (ExpressionElement) obj;
        return this.expression.equals(other.expression);
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public String getExpression() {
        return expression;
    }

    public boolean isCompatibleWith(ExpressionElement other) {
        return getClass().isAssignableFrom(other.getClass())
                || other.getClass().isAssignableFrom(getClass());
    }

    @Override
    public String toString() {
        return getExpression();
    }

    // We assume the two monomials cannot have the same variable part
    @Override
    public int compareTo(ExpressionElement o) {
        if(variables.isEmpty()) return 1;
        if(o.variables.isEmpty()) return -1;
        if(!areVariablesExclusivelySimples() || !o.areVariablesExclusivelySimples()) return 0;

        float selfDegree = getMaxDegree();
        float oDegree = o.getMaxDegree();

        return Float.compare(oDegree, selfDegree);
    }

    public float getMaxDegree() {
        float initial = 0;

        for(Variable var : variables) {
            if(!(var instanceof SimpleVariable)) continue;
            Expression exponent = ((SimpleVariable) var).getExponent();
            if(ExpressionUtils.isANumber(exponent)) {
                float value = Float.parseFloat(exponent.toString());
                if(value > initial) initial = value;
            }
        }
        return initial;
    }

    public boolean areVariablesExclusivelySimples() {
        for (Variable variable : variables) {
            if (variable instanceof IrreducibleVarPart) return false;
        }
        return true;
    }
}
