package net.akami.mask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FormatterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormatterFactory.class);

    public static String removeFractions(String origin) {
        if(!origin.contains("/"))
            return origin;

        while(ExpressionUtils.areEdgesBracketsConnected(origin)) {
            LOGGER.info("{} has connected brackets", origin);
            origin = origin.substring(1, origin.length() - 1);
        }
        if (ExpressionUtils.TRIGONOMETRY_SHORTCUTS.contains(origin))
            return origin;

        StringBuilder builder = new StringBuilder();
        List<String> monomials = ExpressionUtils.toMonomials(origin);
        for (String monomial : monomials) {
            String vars = ExpressionUtils.toVariables(monomial);
            LOGGER.debug("Vars from {} : {}", origin, vars);
            String numericValue = ExpressionUtils.toNumericValue(monomial);
            LOGGER.debug("Treating {}, vars : {}, numericValue : {}", monomial, vars, numericValue);
            if (builder.length() != 0 && !ExpressionUtils.isSigned(numericValue)) {
                builder.append('+');
            }
            builder.append(MathUtils.breakNumericalFraction(numericValue)).append(vars);
        }
        if(builder.length() == 0)
            builder.append(0);
        LOGGER.debug("Removed the fractions, {} became {}", origin, builder);
        return builder.toString();
    }

    public static String formatForCalculations(String origin) {
        origin = formatTrigonometry(ExpressionUtils.cancelMultShortcut(origin));
        return origin;
    }

    public static String formatTrigonometry(String origin) {
        origin = origin
                .replaceAll("\\s", "")
                .replaceAll("s\\*i\\*n\\*\\((.*?)\\)", "\\(\\($1\\)\\*@\\)")
                .replaceAll("c\\*o\\*s\\*\\((.*?)\\)", "\\(\\($1\\)\\*#\\)")
                .replaceAll("t\\*a\\*n\\*\\((.*?)\\)", "\\(\\($1\\)\\*§\\)");
        return origin;
    }

    // TODO : remove 1's in front of variables, remove useless brackets
    public static String formatForVisual(String origin) {
        origin = origin
                .replaceAll("\\((.*?)\\)(\\*@|@)", "sin\\($1\\)")
                .replaceAll("\\((.*?)\\)(\\*#|#)", "cos\\($1\\)")
                .replaceAll("\\((.*?)\\)(\\*§|§)", "tan\\($1\\)");

        return ExpressionUtils.addMultShortcut(origin);
    }
}
