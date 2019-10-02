package net.akami.atosym.expression;

public final class DisplayUtils {

    private DisplayUtils() {
    }

    public static String join(MathObject a, MathObject b, String separator) {

        // TODO : Test if () are needed for the numerator
        return surroundWithParenthesis(a)
                + separator
                + surroundWithParenthesis(b);
    }

    private static String surroundWithParenthesis(MathObject mathObject) {
        return surroundWithParenthesis(mathObject.display());
    }

    public static String surroundWithParenthesis(String str) {
        return '(' + str + ')';
    }
}
