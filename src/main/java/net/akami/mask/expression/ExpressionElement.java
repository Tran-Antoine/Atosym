package net.akami.mask.expression;

import java.util.Collections;
import java.util.List;

public abstract class ExpressionElement {

    protected final List<ExpressionEncapsulator> encapsulators;
    private String finalExpression;

    public ExpressionElement() {
        this(Collections.emptyList());
    }

    public ExpressionElement(List<ExpressionEncapsulator> encapsulators) {
        this.encapsulators = Collections.unmodifiableList(encapsulators);
    }

    public abstract String getRawExpression();

    public final String getExpression() {
        if(finalExpression == null) {
            finalExpression = loadExpression();
        }
        return finalExpression;
    }

    private String loadExpression() {
        StringBuilder builder = new StringBuilder();
        for(int i = encapsulators.size()-1; i >= 0; i--) {
            builder.append(encapsulators.get(i).getEncapsulationString()[0]);
        }

        builder.append(this.getRawExpression());

        for(ExpressionEncapsulator encapsulator : encapsulators) {
            builder.append(encapsulator.getEncapsulationString()[1]);
        }

        return builder.toString();
    }

    public boolean isCompatibleWith(ExpressionElement other) {
        return getClass().equals(other.getClass());
    }

    public boolean isEncapsulated() {
        return !encapsulators.isEmpty();
    }

    // Both encapsulators must be in the same order. sin(x^y) != sin(x)^y
    public boolean hasSameEncapsulationAs(ExpressionElement other) {
        return encapsulators.equals(other.encapsulators);
    }

    public List<ExpressionEncapsulator> getLayers() {
        return encapsulators;
    }
}
