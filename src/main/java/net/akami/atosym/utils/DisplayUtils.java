package net.akami.atosym.utils;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;

public final class DisplayUtils {

    private DisplayUtils() { }

    public static String join(MathObject a, MathObject b, String separator, MathObject parent, InfixNotationDisplayable displayable) {

        StringBuilder builder = new StringBuilder();
        return builder.append(addBracketsIfRequired(a, parent, displayable))
                .append(separator)
                .append(addBracketsIfRequired(b, parent, displayable))
                .toString();
    }

    private static String addBracketsIfRequired(MathObject mathObject, MathObject parent, InfixNotationDisplayable displayable) {
        String display = mathObject.getDisplayer().accept(displayable);

        if(parent.priority() <= mathObject.priority()) {
            return display;
        }

        return surroundWithBrackets(display);
    }

    public static String surroundWithBrackets(String concatWithComma) {
        return '(' + concatWithComma + ')';
    }
}
