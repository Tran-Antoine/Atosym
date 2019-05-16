package net.akami.mask.expression;

import net.akami.mask.operation.MaskContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Monomial implements ExpressionElement {

    private final String expression;
    private final float numericValue;
    private final List<Variable> variables;

    public Monomial(char var, MaskContext context) {
        this(1.0f, new Variable(var, context));
    }

    public Monomial(float numericValue, Variable... variables) {
        this(numericValue, Arrays.asList(variables));
    }

    public Monomial(float numericValue, List<Variable> variables) {
        this.variables = Objects.requireNonNull(Collections.unmodifiableList(variables));
        this.numericValue = numericValue;
        this.expression = loadExpression();
    }

    private String loadExpression() {

        if(variables.isEmpty()) return String.valueOf(numericValue);
        if(numericValue == 1)   return variablesToString();
        if(numericValue == -1)  return '-' + variablesToString();

        return numericValue + variablesToString();
    }

    public boolean hasSameVariablePartAs(Monomial other) {

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

        return count > 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Monomial))
            return false;

        Monomial other = (Monomial) obj;
        return this.expression.equals(other.expression);
    }


    public List<Variable> getVariables() {
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
