package net.akami.atosym.display;

import net.akami.atosym.expression.MathObject;

import java.util.List;

public class FunctionalNotationDisplayable extends SimpleDisplayable {

    public FunctionalNotationDisplayable(List<MathObject> children, String functionName) {
        super(children, functionName);
    }
}
