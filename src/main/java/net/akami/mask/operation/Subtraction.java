package net.akami.mask.operation;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;

import java.util.List;

public class Subtraction extends BinaryOperationHandler {

    private static final Subtraction INSTANCE = new Subtraction();

    @Override
    protected String operate(String a, String b) {
        LOGGER.info("Subtraction process of {} |-| {}: \n", a, b);

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
        return Sum.getInstance().monomialSum(monomials, true);
    }

    @Override
    public String inFormat(String origin) {
        return origin;
    }

    @Override
    public String outFormat(String origin) {
        return FormatterFactory.removeMultiplicationSigns(origin);
    }

    public static Subtraction getInstance() {
        return INSTANCE;
    }
}
