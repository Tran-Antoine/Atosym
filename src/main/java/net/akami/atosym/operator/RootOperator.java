package net.akami.atosym.operator;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.RootMathObject;

import java.util.Collections;
import java.util.List;

public class RootOperator extends MathOperator {

    public RootOperator() {
        super(Collections.singletonList("root"), 2);
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        MathObject base = input.get(0);
        MathObject value = input.get(1);

        if(base.getType() != MathObjectType.NUMBER || value.getType() != MathObjectType.NUMBER) {
            return new RootMathObject(input);
        }

        NumberExpression numericBase = (NumberExpression) base;
        NumberExpression numericValue = (NumberExpression) value;
        float rootValue = root(numericBase.getValue(), numericValue.getValue());
        return new NumberExpression(rootValue);
    }

    private float root(float base, float value) {
        /*float a = 0;
        float b = value;

        while (b-a > 0.0001) {
            float root = (a+b) / 2;
            double result = Math.pow(root, base);

            if(result == value) return root;

            if(result < value) {
                a = root;
            } else {
                b = root;
            }
        }
        return new BigDecimal(b).round(new MathContext(4)).floatValue();*/
        return (float) Math.pow(value, 1/base);
    }
}
