package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LiteralFunction extends MathFunction {

    private String displayName;

    public LiteralFunction(List<MathObject> children, String displayName, int size) {
        super(children, size);
        this.displayName = displayName;
    }

    @Override
    public String display() {
        String concatWithComma = children.stream().map(MathObject::display).collect(Collectors.joining(", "));
        return displayName + DisplayUtils.surroundWithParenthesis(concatWithComma);
    }
}
