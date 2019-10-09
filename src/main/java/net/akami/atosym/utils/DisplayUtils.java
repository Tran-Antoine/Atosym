package net.akami.atosym.utils;

import net.akami.atosym.expression.MathObject;

public final class DisplayUtils {

    private DisplayUtils() { }

    public static String join(MathObject a, MathObject b, String separator, MathObject parent) {

        StringBuilder builder = new StringBuilder();
        return builder.append(addBracketsIfRequired(a, parent))
                .append(separator)
                .append(addBracketsIfRequired(b, parent))
                .toString();
    }

    private static String addBracketsIfRequired(MathObject mathObject, MathObject parent) {
        String display = mathObject.display();

        if(parent.priority() <= mathObject.priority()) {
            return display;
        }

        return surroundWithBrackets(display);
    }

    public static String surroundWithBrackets(String concatWithComma) {
        return '(' + concatWithComma + ')';
    }
}
