package net.akami.atosym.merge.property;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Monomial;
import net.akami.atosym.expression.NumberElement;
import net.akami.atosym.utils.ExpressionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class NumericalDivisionProperty extends OverallMergeProperty<Monomial, List<Monomial>> {

    private MaskContext context;

    public NumericalDivisionProperty(Monomial p1, Monomial p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        return ExpressionUtils.isANumber(p1) && ExpressionUtils.isANumber(p2);
    }

    @Override
    protected List<Monomial> computeResult() {
        float a = p1.getNumericValue();
        float b = p2.getNumericValue();
        BigDecimal bigA = new BigDecimal(a, context.getMathContext());
        BigDecimal bigB = new BigDecimal(b, context.getMathContext());
        return Collections.singletonList(new NumberElement(bigA.divide(bigB, context.getMathContext()).floatValue()));
    }
}
