package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;
import java.util.stream.Collectors;

public class InfixNotationDisplayable implements Displayable {

    public static final InfixNotationDisplayable EMPTY_INSTANCE = new InfixNotationDisplayable();

    public List<String> toStringList(List<MathObject> target, InfixNotationDisplayable displayable) {
        return target
                .stream()
                .map(o -> o.getDisplayer().accept(displayable))
                .collect(Collectors.toList());
    }
}
