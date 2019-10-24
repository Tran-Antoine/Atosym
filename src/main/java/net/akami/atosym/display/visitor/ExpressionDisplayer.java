package net.akami.atosym.display.visitor;

import net.akami.atosym.display.FunctionalNotationDisplayable;
import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.display.PrefixNotationDisplayable;

public class ExpressionDisplayer implements DisplayerVisitor {

    private Object value;

    public ExpressionDisplayer(Object value) {
        this.value = value;
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        return value.toString();
    }

    @Override
    public String accept(FunctionalNotationDisplayable displayable) {
        return value.toString();
    }

    @Override
    public String accept(PrefixNotationDisplayable displayable) {
        return value.toString();
    }
}
