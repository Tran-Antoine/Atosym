package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;
import java.util.stream.Collectors;

public class InfixNotationDisplayable implements Displayable {

    public static final InfixNotationDisplayable EMPTY_INSTANCE = new InfixNotationDisplayable();

    public List<String> toStringList(List<MathObject> target) {
        return target
                .stream()
                .map(o -> o.getDisplayer().accept(this))
                .collect(Collectors.toList());
    }

    public String join(MathObject a, MathObject b, String separator, MathObject parent) {

        StringBuilder builder = new StringBuilder();
        return builder.append(addBracketsIfRequired(a, parent))
                .append(separator)
                .append(addBracketsIfRequired(b, parent))
                .toString();
    }

    private String addBracketsIfRequired(MathObject mathObject, MathObject parent) {
        String display = mathObject.getDisplayer().accept(this);

        if(parent.priority() <= mathObject.priority()) {
            return display;
        }

        return DisplayUtils.surroundWithBrackets(display);
    }
}
