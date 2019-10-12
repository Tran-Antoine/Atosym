package net.akami.atosym.utils;

public final class DisplayUtils {

    private DisplayUtils() { }

    public static String surroundWithBrackets(String concatWithComma) {
        return '(' + concatWithComma + ')';
    }
}
