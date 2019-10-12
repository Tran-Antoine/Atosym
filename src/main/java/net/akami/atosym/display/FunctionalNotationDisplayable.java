package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionalNotationDisplayable implements Displayable {

    public static final FunctionalNotationDisplayable EMPTY_INSTANCE = new FunctionalNotationDisplayable();

    public List<String> toStringList(List<MathObject> target, FunctionalNotationDisplayable displayable) {
        return target
                .stream()
                .map(o -> o.getDisplayer().accept(displayable))
                .collect(Collectors.toList());
    }
}
