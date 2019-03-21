package net.akami.mask.utils;

import net.akami.mask.tree.BinaryTree;
import net.akami.mask.tree.Branch;
import net.akami.mask.tree.FormatterTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import static net.akami.mask.utils.ExpressionUtils.MATH_SIGNS;
import static net.akami.mask.utils.ExpressionUtils.VARIABLES;

public class FormatterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormatterFactory.class);
    private static final StringBuilder BUILDER = new StringBuilder();

    public static String removeFractions(String origin) {
        if(!origin.contains("/"))
            return origin;

        while(ExpressionUtils.areEdgesBracketsConnected(origin, true)) {
            LOGGER.info("{} has connected brackets", origin);
            origin = origin.substring(1, origin.length() - 1);
        }
        if (ExpressionUtils.isTrigonometricShortcut(origin))
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
        origin = formatTrigonometry(addMultiplicationSigns(origin, false));
        return origin;
    }

    public static String formatTrigonometry(String origin) {
        origin = origin
                .replaceAll("\\s", "")
                .replaceAll("s\\*i\\*n\\*\\((.*?)\\)", "\\(\\($1\\)@\\)")
                .replaceAll("c\\*o\\*s\\*\\((.*?)\\)", "\\(\\($1\\)#\\)")
                .replaceAll("t\\*a\\*n\\*\\((.*?)\\)", "\\(\\($1\\)§\\)");
        return origin;
    }

    // TODO : remove 1's in front of variables, remove useless brackets
    public static String formatForVisual(String origin) {
        /*origin = origin
                .replaceAll("\\(\\((.*?)\\)(\\*@|@)\\)", "sin\\($1\\)")
                .replaceAll("\\(\\((.*?)\\)(\\*#|#)\\)", "cos\\($1\\)")
                .replaceAll("\\(\\((.*?)\\)(\\*§|§)\\)", "tan\\($1\\)");

        return removeMultiplicationSigns(origin);*/
        BinaryTree tree = new FormatterTree(origin);
        return tree.merge();
    }

    public static String removeMultiplicationSigns(String self) {
        clearBuilder();
        BUILDER.append(self);
        for (int i = 1; i < self.length() - 1; i++) {
            char c = self.charAt(i);

            if (c == '*' && VARIABLES.contains(String.valueOf(self.charAt(i + 1)))) {
                BUILDER.setCharAt(i, '$');
            }

        }
        return BUILDER.toString().replace("$", "");
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    public static String addMultiplicationSigns(String self, boolean addForTrigonometry) {
        self = self.replaceAll("\\s", "");
        clearBuilder();

        for (int i = 0; i < self.length(); i++) {
            String c = String.valueOf(self.charAt(i));
            boolean isVar = VARIABLES.contains(c);

            if (isVar && i != 0 && !MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1)))) {
                BUILDER.append("*").append(c);
            } else if (i != 0 && c.equals("(") &&
                    (self.charAt(i - 1) == ')' || !MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1))))) {
                BUILDER.append("*").append(c);
            } else if (i != 0 && self.charAt(i - 1) == ')' && !MATH_SIGNS.contains(c) &&
                    (!ExpressionUtils.isTrigonometricShortcut(c) || addForTrigonometry)) {
                BUILDER.append("*").append(c);
            } else {
                BUILDER.append(c);
            }
        }
        LOGGER.error("Converted : " + self + " to " + BUILDER.toString());
        return BUILDER.toString();
    }

    public static String addAllCoefficients(String origin) {

        clearBuilder();
        for(int i = 0; i<origin.length(); i++) {
            String current = String.valueOf(origin.charAt(i));
            String previous = i == 0 ? "$" : String.valueOf(origin.charAt(i-1));

            if(VARIABLES.contains(current) && (ExpressionUtils.MATH_SIGNS.contains(previous) || i== 0)) {
                BUILDER.append('1');
            }

            BUILDER.append(current);
        }
        return BUILDER.toString();
    }
}
