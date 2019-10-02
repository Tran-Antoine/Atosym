package net.akami.atosym.function;

import net.akami.atosym.expression.MathObject;

public abstract class NumberRequiredFunction extends MathOperator {

    public NumberRequiredFunction(String name, int argsLength) {
        super(name, argsLength);
    }

    @Override
    protected MathObject operate(MathObject... input) {
        if(areAllNumbers(input)) return encapsulate(input);

        return computeNumbers(input[0]);
    }

    private MathObject encapsulate(MathObject... unique) {
        /*List<MathObject> elements = unique.getElements();
        List<ExpressionOverlay> overlays = Collections.singletonList(this);

        IntricateVariable var = new IntricateVariable(elements, overlays);
        return MathObject.of(new Monomial(1, var));*/
        return null;
    }

    private MathObject computeNumbers(MathObject unique) {
        /*double result = function().compute(Double.parseDouble(unique.toString()));
        BigDecimal decimal = new BigDecimal(result).setScale(6, BigDecimal.ROUND_HALF_UP);
        return Expression.of(decimal.floatValue());*/
        return null;
    }

    private boolean areAllNumbers(MathObject... input) {
        for(MathObject exp : input) {
            //if(!ExpressionUtils.isANumber(exp)) return false;
        }
        return true;
    }

    protected abstract DoubleOperation function();
}
