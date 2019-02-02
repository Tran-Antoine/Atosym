package net.akami.mask.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathExpression {

    private static final String NUMBERS = "0123456789";
    private final String expression;
    private final Set<Character> variables;

    public MathExpression(final String expression) {
        this.expression = expression;
        this.variables = createVariables();
    }

    private Set<Character> createVariables() {
        Set<Character> variables = new HashSet<>();

        for(int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(!NUMBERS.contains(String.valueOf(c))) {
                variables.add(c);
            }
        }
        return variables;
    }

    public int getVariablesAmount() {
        return variables.size();
    }
}
