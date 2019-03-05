package net.akami.mask.utils;

import java.util.List;

public class FormatterFactory {

    private static final StringBuilder BUILDER = new StringBuilder();

    public static String removeFractions(String origin) {
        clearBuilder();
        List<String> monomials = ExpressionUtils.toMonomials(origin);
        for(String monomial : monomials) {
            String vars = ExpressionUtils.toVariables(monomial);
            String numericValue = ExpressionUtils.toNumericValue(monomial);
            if(BUILDER.length() != 0 && !ExpressionUtils.isSigned(numericValue)) {
                BUILDER.append('+');
            }
            BUILDER.append(MathUtils.breakNumericalFraction(numericValue)).append(vars);
        }
        return BUILDER.toString();
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
