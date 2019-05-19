package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Monomial extends ExpressionElement<Monomial> {

    private final String expression;
    private final float numericValue;
    private final List<Variable> variables;


    public Monomial(char var, MaskContext context) {
        this(1.0f, new SimpleVariable(var, context));
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

        if(numericValue == 0) return "";
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
        if(expression.startsWith("(") && expression.endsWith(")")) count = 0;

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
    public String getRawExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return getRawExpression();
    }

    // We assume the two monomials cannot have the same variable part
    @Override
    public int compareTo(Monomial o) {
        if(variables.isEmpty()) return 1;
        if(o.variables.isEmpty()) return -1;
        if(!areVariablesExclusivelySimples() || !o.areVariablesExclusivelySimples()) return 0;

        float selfDegree = getMaxDegree();
        float oDegree = o.getMaxDegree();

        if(selfDegree > oDegree) return -1;
        if(selfDegree < oDegree) return 1;

        return 0;
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
            if (variable instanceof ComposedVariable) return false;
        }
        return true;
    }
}
