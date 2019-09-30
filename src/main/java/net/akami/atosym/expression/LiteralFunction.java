package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LiteralFunction extends MathFunction {

    private String name;

    public LiteralFunction(MathOperator operator, List<MathObject> children, String name) {
        super(operator, children);
        this.name = name;
    }

    @Override
    public String display() {
        String concatWithComma = String.join(", ", children.stream().map(MathObject::display).collect(Collectors.toList()));
        return name + DisplayUtils.surroundWithParenthesis(concatWithComma);
    }
}
