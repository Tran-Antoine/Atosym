package net.akami.mask.expression;

import java.math.BigDecimal;

public class Monomial implements ExpressionElement {

    private final String expression;
    private final float numericValue;
    private final Variable[] variables;

    public Monomial(float numericValue, Variable... variables) {
        this.variables = variables;
        this.numericValue = numericValue;
        this.expression = loadExpression();
    }

    private String loadExpression() {

        if(variables == null)  return String.valueOf(numericValue);
        if(numericValue == 1)  return variablesToString();
        if(numericValue == -1) return '-' + variablesToString();

        return numericValue + variablesToString();
    }

    public boolean hasSameVariablePartAs(Monomial other) {

        if(this.variables == other.variables) return true;
        if(this.variables == null || other.variables == null) return false;
        if(this.variables.length != other.variables.length) return false;

        for(int i = 0; i < variables.length; i++) {
            if(!this.variables[i].equals(other.variables[i]))
                return false;
        }
        return true;
    }

    public float getNumericValue() {
        return numericValue;
    }

    public String variablesToString() {
        if(variables == null) return "";

        StringBuilder builder = new StringBuilder();
        for(Variable var : variables)
            builder.append(var.getExpression());
        return builder.toString();
    }

    public boolean requiresBrackets() {
        int count = 0;
        if(Math.abs(numericValue) != 1)                count++;
        if(variables != null && variables.length != 0) count++;
        if(variables != null && variables.length > 1)  count++;

        return count > 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Monomial))
            return false;

        Monomial other = (Monomial) obj;
        return this.expression.equals(other.expression);
    }


    public Variable[] getVariables() {
        return variables;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return getExpression();
    }
}
