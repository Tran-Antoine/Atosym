package net.akami.mask.expression;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class VariablePart implements Iterable<Variable> {

    private final List<Variable> variables;
    private final String expression;

    public VariablePart(List<Variable> variables) {
        this.variables = Collections.unmodifiableList(variables);
        this.expression = loadExpression();
    }

    private String loadExpression() {
        StringBuilder builder = new StringBuilder();

        for(Variable variable : variables) {
            builder.append(variable.getExpression());
        }
        return builder.toString();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return variables.size();
    }

    public Variable get(int index) {
        if(index < 0) return variables.get(size() - index);
        else return variables.get(index);
    }

    public String getExpression() {
        return expression;
    }

    public boolean isSimple() {
        return variables.size() == 1 && variables.get(0) instanceof SingleCharVariable;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof VariablePart)) return false;
        return this.variables.equals(((VariablePart) obj).variables);
    }

    @Override
    public Iterator<Variable> iterator() {
        return variables.iterator();
    }
}
