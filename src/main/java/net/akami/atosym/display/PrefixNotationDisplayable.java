package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;
import java.util.stream.Collectors;

public class PrefixNotationDisplayable {

    public static final PrefixNotationDisplayable EMPTY_INSTANCE = new PrefixNotationDisplayable();

    public List<String> toStringList(List<MathObject> target) {
        return target
                .stream()
                .map(o -> o.getDisplayer().accept(this))
                .collect(Collectors.toList());
    }

    public String toShortcut(String functionName) {
        switch (functionName) {
            case "sum": return "+";
            case "sub": return "-";
            case "mult": return "*";
            case "div": return "/";
            case "pow": return "^";
            default: return functionName;
        }
    }
}
