package net.akami.mask.utils;

public class FormatterFactory {

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

    public static String addMultiplicationSigns(String self, boolean addForTrigonometry) {

        StringBuilder builder = new StringBuilder();
        self = self.replaceAll("\\s", "");

        for (int i = 0; i < self.length(); i++) {
            String c = String.valueOf(self.charAt(i));
            boolean isVar = ExpressionUtils.VARIABLES.contains(c);

            if (isVar && i != 0 && !ExpressionUtils.MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1)))) {
                builder.append("*").append(c);
            } else if (i != 0 && c.equals("(") &&
                    (self.charAt(i - 1) == ')' || !ExpressionUtils.MATH_SIGNS.contains(String.valueOf(self.charAt(i - 1))))) {
                builder.append("*").append(c);
            } else if (i != 0 && self.charAt(i - 1) == ')' && !ExpressionUtils.MATH_SIGNS.contains(c) &&
                    (!ExpressionUtils.isTrigonometricShortcut(c) || addForTrigonometry)) {
                builder.append("*").append(c);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
    public static String removeMultiplicationSigns(String self) {
        StringBuilder builder = new StringBuilder();
        builder.append(self);
        for (int i = 1; i < self.length() - 1; i++) {
            char c = self.charAt(i);

            if (c == '*' && ExpressionUtils.VARIABLES.contains(String.valueOf(self.charAt(i + 1)))) {
                builder.setCharAt(i, '$');
            }

        }
        return builder.toString().replace("$", "");
    }


    public static String removeEdgeBrackets(String exp, boolean falseIfTrigonometry) {
        while(areEdgesBracketsConnected(exp, falseIfTrigonometry))
            exp = exp.substring(1, exp.length()-1);
        return exp;
    }

    public static boolean areEdgesBracketsConnected(String exp, boolean falseIfTrigonometry) {
        if (exp.isEmpty() || exp.charAt(0) != '(') {
            return false;
        }

        if(exp.length() > 1 &&
                ExpressionUtils.isTrigonometric(String.valueOf(exp.charAt(exp.length()-2))) && falseIfTrigonometry) {
            return false;
        }

        int left = 0;
        for (int i = 1; i < exp.length() - 1; i++) {
            if (exp.charAt(i) == ')') {
                left--;
            } else if (exp.charAt(i) == '(') {
                left++;
            }
            if (left < 0) {
                break;
            }
        }
        if (left >= 0) {
            return true;
        }
        return false;
    }
}
