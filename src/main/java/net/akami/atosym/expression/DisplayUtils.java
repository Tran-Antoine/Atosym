package net.akami.atosym.expression;

public final class DisplayUtils {

    private DisplayUtils(){}

    public static String join(MathObject a, MathObject b, String separator) {

        StringBuilder builder = new StringBuilder();
        // TODO : Test if () are needed for the numerator
        builder
                .append('(')
                .append(a.display()).append(')').append(separator).append('(')
                .append(b.display())
                .append(")");
        return builder.toString();
    }
}
