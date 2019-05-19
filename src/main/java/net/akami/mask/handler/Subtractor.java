package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.core.MaskContext;

import java.util.ArrayList;
import java.util.List;

public class Subtractor extends BinaryOperationHandler<Expression> {

    public Subtractor(MaskContext context) {
        super(context);
    }

    @Override
    protected Expression operate(Expression a, Expression b) {
        LOGGER.info("Subtractor process of {} |-| {}: \n", a, b);

        List<ExpressionElement> opposite = new ArrayList<>(b.length());

        for(ExpressionElement bElement : b.getElements()) {
            Multiplier multiplier = context.getBinaryOperation(Multiplier.class);
            opposite.add(multiplier.simpleMult(new NumberElement(-1.0f), bElement));
        }

        return context.getBinaryOperation(Adder.class).operate(a, new Expression(opposite));
    }

    @Override
    public Expression inFormat(Expression origin) {
        return origin;
    }

    @Override
    public Expression outFormat(Expression origin) {
        return origin;
    }
}
