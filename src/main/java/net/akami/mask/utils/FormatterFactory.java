package net.akami.mask.utils;

import java.util.List;

public class FormatterFactory {

    private static final StringBuilder BUILDER = new StringBuilder();

    public static String removeFractions(String origin) {

        if (ExpressionUtils.TRIGONOMETRY_SHORTCUTS.contains(origin))
            return origin;

        clearBuilder();
        List<String> monomials = ExpressionUtils.toMonomials(origin);
        for (String monomial : monomials) {
            String vars = ExpressionUtils.toVariables(monomial);
            String numericValue = ExpressionUtils.toNumericValue(monomial);
            if (BUILDER.length() != 0 && !ExpressionUtils.isSigned(numericValue)) {
                BUILDER.append('+');
            }
            BUILDER.append(MathUtils.breakNumericalFraction(numericValue)).append(vars);
        }
        return BUILDER.toString();
    }

    public static String formatForCalculations(String origin) {
        origin = ExpressionUtils.cancelMultShortcut(origin)
                .replaceAll("\\s", "")
                .replaceAll("s\\*i\\*n\\*\\((.*?)\\)", "\\(\\($1\\)\\*@\\)")
                .replaceAll("c\\*o\\*s\\*\\((.*?)\\)", "\\(\\($1\\)\\*#\\)")
                .replaceAll("t\\*a\\*n\\*\\((.*?)\\)", "\\(\\($1\\)\\*§\\)");
        return origin;
    }

    // TODO : remove 1's in front of variables, remove useless brackets
    public static String formatForVisual(String origin) {
        origin = origin
                .replaceAll("\\((.*?)\\)(\\*@|@)", "sin$1")
                .replaceAll("\\((.*?)\\)(\\*#|#)", "cos$1")
                .replaceAll("\\((.*?)\\)(\\*§|§)", "tan$1");

        return ExpressionUtils.addMultShortcut(origin);
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
