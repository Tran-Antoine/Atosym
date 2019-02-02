package net.akami.mask;

import java.util.ArrayList;
import java.util.List;

public class MathExpression {

    private static final String NUMBERS = "0123456789";
    private final String expression;
    private final List<Character> variables;

    public MathExpression(final String expression) {
        this.expression = expression;
        this.variables = createVariables();
    }

    private List<Character> createVariables() {
        List<Character> variables = new ArrayList<>();

        for(int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(!NUMBERS.contains(String.valueOf(c)) && !variables.contains(c)) {
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
        System.out.println(replaced);
        return new RestCalculation(Reducer.reduce(replaced));
    }

    private String replace(char var, float value, String toReplace) {

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < toReplace.length(); i++) {
            if(toReplace.charAt(i) == var && i!= 0) {

                if(NUMBERS.contains(String.valueOf(toReplace.charAt(i-1)))) {
                    //the char before the variable is a number. 4x obviously means 4*x
                    builder.append("*"+value);
                } else {
                    // No number before the variable, for instance 3+x
                    builder.append(value);
                }
            } else {
                // No variable found, we just add the same char
                builder.append(toReplace.charAt(i));
            }
        }
        return builder.toString();
    }

    public int getVariablesAmount() {
        return variables.size();
    }
}
