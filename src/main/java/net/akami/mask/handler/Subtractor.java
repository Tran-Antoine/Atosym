package net.akami.mask.handler;

import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.util.List;

public class Subtractor extends BinaryOperationHandler {

    public Subtractor(MaskContext context) {
        super(context);
    }

    @Override
    protected String operate(String a, String b) {
        LOGGER.info("Subtractor process of {} |-| {}: \n", a, b);

        List<String> monomials = ExpressionUtils.toMonomials(a);
        List<String> bMonomials = ExpressionUtils.toMonomials(b);

        // Changes the sign of the monomials that need to be subtracted
        for (int i = 0; i < bMonomials.size(); i++) {
            String m = bMonomials.get(i);

            if (m.startsWith("+")) {
                bMonomials.set(i, "-" + m.substring(1));
            } else if (m.startsWith("-")) {
                bMonomials.set(i, "+" + m.substring(1));
            } else {
                bMonomials.set(i, "-" + m);
            }
        }
        monomials.addAll(bMonomials);
        return MathUtils.sum(monomials, context);
    }

    @Override
    public String inFormat(String origin) {
        return origin;
    }

    @Override
    public String outFormat(String origin) {
        return FormatterFactory.removeMultiplicationSigns(origin);
    }
}
