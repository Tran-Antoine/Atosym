package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;

public class LiteralFunctionDisplayer extends SimpleDisplayerVisitor {

    @Override
    public String accept(InfixNotationDisplayable displayer) {
        return super.functionalDisplay(displayer);
    }
}
