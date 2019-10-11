package net.akami.atosym.expression;

import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class PowerMathObject extends FunctionObject {

    public PowerMathObject(List<MathObject> children) {
        super(children, -1);
    }

    // TODO : Change so that it works for a^b^c
    @Override
    public String display() {
        return DisplayUtils.join(children.get(0), children.get(1), "^", this, displayer);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.POW;
    }

    @Override
    public int priority() {
        return 2;
    }
}
