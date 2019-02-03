package net.akami.mask;

import java.util.ArrayList;
import java.util.List;

public class MaskExpression {

    private static final String NON_VARIABLES = "0123456789+-*=^";
    private final String expression;
    private final List<Character> variables;

    public MaskExpression(final String expression) {
        this.expression = expression;
        this.variables = createVariables();
    }

    private List<Character> createVariables() {
        List<Character> variables = new ArrayList<>();

        for(int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(!NON_VARIABLES.contains(String.valueOf(c)) && !variables.contains(c)) {
                variables.add(c);
            }
        }
        return variables;
    }

    public RestCalculation imageFor(float... values) {

        String replaced = expression;
        for(int i = 0; i < values.length; i++) {
            char var = variables.get(i);
            replaced = replace(var, values[i], replaced);
        }
        return Reducer.reduce(replaced);
    }

    private String replace(char var, float value, String self) {

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < self.length(); i++) {
            if(self.charAt(i) == var && i!= 0) {

                if(NON_VARIABLES.contains(String.valueOf(self.charAt(i-1)))) {
                    //the char before the variable is a number. 4x obviously means 4*x
                    builder.append("*"+value);
                } else {
                    // No number before the variable, for instance 3+x
                    builder.append(value);
                }
            } else {
                // No variable found, we just add the same char
                builder.append(self.charAt(i));
            }
        }
        return builder.toString();
    }

    public int getVariablesAmount() {
        return variables.size();
    }
}
