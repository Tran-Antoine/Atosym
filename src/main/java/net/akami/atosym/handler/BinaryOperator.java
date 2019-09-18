package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.function.MathOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BinaryOperator extends MathOperator {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperator.class);

    public BinaryOperator(String name) {
        super(name, 2);
    }

    @Override
    protected Expression operate(Expression... input) {
        return binaryOperate(input[0], input[1]);
    }

    protected abstract Expression binaryOperate(Expression a, Expression b);

    public static Set<BinaryOperator> generateDefaultBinaryOperators(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new SumOperator(context),
                new SubOperator(context),
                new MultOperator(context),
                new DivOperator(context),
                new PowerOperator(context)
        ));
    }
}
