package net.akami.atosym.utils;

import net.akami.atosym.expression.Expression;
import net.akami.atosym.overlay.ExpressionOverlay;

import java.util.List;
import java.util.function.Function;

public final class ExpressionUtils {

    public static final String MATH_SIGNS = "+-*/^()";
    // 'E' deliberately missing, because it corresponds to "*10^x"
    public static final String VARIABLES = "abcdefghijklmnopqrstuvwxyzABCDFGHIJKLMNOPQRSTUVWXYZ";
    public static final String TRIGONOMETRY_SHORTCUTS = "@#§";
    public static final String NUMBERS = "0123456789";

    public static boolean isANumber(Expression exp) {
        if(exp.length() != 1) return false;
        return isANumber(exp.get(0));
    }

    public static boolean isANumber(Monomial element) {
        List<Variable> vars = element.getVarPart().getVariables();
        return vars.size() == 0;
    }
    public static boolean isANumber(String exp) {
        if (exp.length() == 0)
            return false;
        return exp.matches("(\\+|-|)[\\d]+(\\.[\\d]+|)") || NUMBERS.contains(exp);
    }

    public static boolean isAnInteger(Expression exp) {
        if(exp.length() != 1) return false;
        return isAnInteger(exp.get(0));
    }

    public static boolean isAnInteger(Monomial element) {
        List<Variable> vars = element.getVarPart().getVariables();
        return vars.size() == 0 && element.getNumericValue() % 1 == 0;
    }

    public static String encapsulate(List<Monomial> elements, List<ExpressionOverlay> layers) {
        StringBuilder builder = new StringBuilder();
        for(int i = layers.size()-1; i >= 0; i--) {
            builder.append(layers.get(i).getEncapsulationString(elements, i, layers)[0]);
        }

        builder.append(chainElements(elements, Monomial::getExpression));

        int i = 0;
        for(ExpressionOverlay encapsulator : layers) {
            builder.append(encapsulator.getEncapsulationString(elements, i++, layers)[1]);
        }

        return builder.toString();
    }

    public static String chainElements(List<Monomial> elements, Function<Monomial, String> expFunction) {
        StringBuilder builder = new StringBuilder();
        for(Monomial element : elements) {
            String expression = expFunction.apply(element);
            if(expression.isEmpty()) continue;

            if(builder.length() == 0 || ExpressionUtils.isSigned(expression)) {
                builder.append(expression);
            } else {
                builder.append('+').append(expression);
            }
        }
        return builder.toString();
    }

    public static boolean isSurroundedByParentheses(int index, String exp) {

        int b = FormatterFactory.areEdgesBracketsConnected(exp, false) ? 1 : 0;
        // In case the exp is 5*-3 or 5/-3
        if (index > 0+b && (exp.charAt(index - 1-b) == '/' || exp.charAt(index - 1-b) == '*')) {
            return true;
        }

        int leftParenthesis = 0;

        for (int i = 0+b; i < exp.length()-b; i++) {
            if (exp.charAt(i) == '(') {
                leftParenthesis++;
            }

            if (exp.charAt(i) == ')') {
                leftParenthesis--;
            }
            if (leftParenthesis > 0 && i == index) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSigned(String exp) {
        return exp.charAt(0) == '+' || exp.charAt(0) == '-';
    }

    public static boolean isTrigonometricShortcut(String exp) {
        return exp.length() == 1 && TRIGONOMETRY_SHORTCUTS.contains(exp);
    }
    public static boolean isTrigonometric(String exp) {
        return exp.contains("@") || exp.contains("#") || exp.contains("§");
    }
}
