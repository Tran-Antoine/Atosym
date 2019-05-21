package net.akami.mask.expression;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Monomial implements Comparable<Monomial> {

    private final String expression;
    private final float numericValue;
    private final VariablePart varPart;

    public Monomial(float numericValue, Variable variable) {
        this(numericValue, Collections.singletonList(variable));
    }

    public Monomial(float numericValue, List<Variable> variables) {
        this(numericValue, new VariablePart(Objects.requireNonNull(variables)));
    }

    public Monomial(float numericValue, VariablePart varPart) {
        this.numericValue = numericValue;
        this.varPart = varPart;
        this.expression = loadExpression();
    }

    private String loadExpression() {

        if(numericValue == 0)  return "";
        if(varPart.isEmpty())  return String.valueOf(numericValue);
        if(numericValue == 1)  return variablesToString();
        if(numericValue == -1) return '-' + variablesToString();

        return numericValue + variablesToString();
    }

    public boolean hasSameVariablePartAs(Monomial other) {

        if(this.varPart.size() != other.varPart.size()) return false;

        for(int i = 0; i < varPart.size(); i++) {
            if(!this.varPart.get(i).equals(other.varPart.get(i)))
                return false;
        }
        return true;
    }

    public float getNumericValue() {
        return numericValue;
    }

    public String variablesToString() {
        if(varPart.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();
        for(Variable var : varPart)
            builder.append(var.getExpression());
        return builder.toString();
    }

    public boolean requiresBrackets() {
        int count = 0;
        if(Math.abs(numericValue) != 1) count++;
        if(varPart.size() != 0)       count++;
        if(varPart.size() > 1)        count++;
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

    public VariablePart getVarPart() {
        return varPart;
    }

    public String getExpression() {
        return expression;
    }

    public boolean isCompatibleWith(Monomial other) {
        return getClass().isAssignableFrom(other.getClass())
                || other.getClass().isAssignableFrom(getClass());
    }

    @Override
    public String toString() {
        return getExpression();
    }

    // We assume the two monomials cannot have the same variable part
    @Override
    public int compareTo(Monomial o) {
        if(varPart.isEmpty()) return 1;
        if(o.varPart.isEmpty()) return -1;
        if(!areVariablesExclusivelySimples() || !o.areVariablesExclusivelySimples()) return 0;

        float selfDegree = getMaxDegree();
        float oDegree = o.getMaxDegree();

        return Float.compare(oDegree, selfDegree);
    }

    public float getMaxDegree() {
        return 0;
    }

    public boolean areVariablesExclusivelySimples() {
        for (Variable variable : varPart) {
            if (variable instanceof ComplexVariable) return false;
        }
        return true;
    }
}
