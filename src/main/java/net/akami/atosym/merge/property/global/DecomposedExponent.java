package net.akami.atosym.merge.property.global;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.merge.property.RestartApplicant;

import java.util.Collections;
import java.util.List;

public class DecomposedExponent {

    private MathObject origin;
    protected List<MathObject> exponent;
    protected MathObject base;

    protected MaskContext context;

    public DecomposedExponent(MathObject origin, MaskContext context) {
        this.origin = origin;
        this.context = context;
        splitBaseAndExponent();
    }

    private void splitBaseAndExponent() {
        if(origin.getType() == MathObjectType.POW) {
            ExponentMathObject powerObject = (ExponentMathObject) origin;
            this.exponent = powerObject.getChildrenFraction(1, -1);
            this.base = powerObject.getChild(0);
            return;
        }
        this.exponent = Collections.singletonList(MathObject.NEUTRAL_POW);
        this.base = origin;
    }

    public MathObject exponentToSingleObject() {
        if(exponent.size() == 1) {
            return exponent.get(0);
        }
        return new ExponentMathObject(exponent, context);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DecomposedExponent) {
            DecomposedExponent other = (DecomposedExponent) obj;
            return baseEquals(other) && exponent.equals(other.exponent);
        }
        return false;
    }

    public boolean baseEquals(DecomposedExponent other) {
        return base.equals(other.base);
    }

    public MathObject getBase() {
        return base;
    }
}
