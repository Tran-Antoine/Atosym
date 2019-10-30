package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.FactorialMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.utils.NumericUtils;

import java.util.Collections;
import java.util.List;

public class FactorialOperator extends MathOperator {

    public FactorialOperator(MaskContext context) {
        super(Collections.singletonList("!"), 1, context);
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        MathObject single = input.get(0);
        if(single.getType() != MathObjectType.NUMBER) {
            return new FactorialMathObject(single, context);
        }

        NumberExpression numberObject = (NumberExpression) single;
        float value = numberObject.getValue();
        if(value % 1 != 0) {
            throw new ArithmeticException("Unable to determine the factorial of a non-integer");
        }
        int intValue = (int) value;
        return new NumberExpression(factorial(intValue));
    }

    private float factorial(int value) {
        return factorial(value, value);
    }

    private float factorial(int initialValue, float current) {
        if(initialValue == 0) return 1;
        if(initialValue == 1) return current;
        initialValue = (int) NumericUtils.sub(initialValue, 1, context);
        current = NumericUtils.mult(current, initialValue, context);
        return factorial(initialValue, current);
    }
}
