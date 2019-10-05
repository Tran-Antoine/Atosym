package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;
import java.util.stream.Collectors;

public abstract class LiteralFunction<T extends MathOperator> extends MathFunction<T> {

    public LiteralFunction(T operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        String concatWithComma = String.join(", ", children.stream().map(MathObject::display).collect(Collectors.toList()));
        return operator.getNames().get(0) + DisplayUtils.surroundWithParenthesis(concatWithComma);
    }
}
