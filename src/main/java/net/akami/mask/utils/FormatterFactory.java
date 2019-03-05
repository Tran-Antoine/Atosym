package net.akami.mask.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /*public static String deleteMathShortcuts(String origin) {
        origin = origin.replaceAll("\\s", "");
        Pattern pattern = Pattern.compile("(cos\\()(.+)(\\))");
        Matcher matcher = pattern.matcher(origin);
        while(matcher.find()) {
            System.out.println(matcher.replaceAll(matcher.group(2)));
        }
        return ExpressionUtils.cancelMultShortcut(origin);
    }*/

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }
}
