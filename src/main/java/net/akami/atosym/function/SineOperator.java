package net.akami.atosym.function;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.SineMathObject;

import java.util.Collections;
import java.util.List;

public class SineOperator extends MathOperator {

    public SineOperator() {
        super(Collections.singletonList("sin"), 1);
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        MathObject unique = input.get(0);

        if(unique.getType() == MathObjectType.NUMBER) {
            NumberExpression exp = (NumberExpression) unique;
            return new NumberExpression((float) Math.sin(exp.getValue()));
        }

        return new SineMathObject(this, input);
    }
}
