package net.akami.mask.expression;

public class Monomial implements ExpressionElement {

    private String expression;
    private float numericValue;
    private Variable[] variables;

    public Monomial(float numericValue, Variable[] variables) {
        this.variables = variables;
        this.numericValue = numericValue;
        this.expression = loadExpression();
    }

    private String loadExpression() {
        if(variables == null)
            return String.valueOf(numericValue);

        if(numericValue == 1)
            return variablesToString();

        return numericValue + variablesToString();
    }

    public boolean hasSameVariablePartAs(Monomial other) {

        if(this.variables == other.variables)
            return true;

        if(this.variables.length != other.variables.length)
            return false;

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

        if(variables == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for(Variable var : variables)
            builder.append(var.getExpression());

        return builder.toString();
    }

    public Variable[] getVariables() {
        return variables;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
