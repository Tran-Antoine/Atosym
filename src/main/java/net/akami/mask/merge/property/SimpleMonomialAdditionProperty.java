package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;

import java.math.BigDecimal;
import java.util.List;

public class SimpleMonomialAdditionProperty extends ElementSequencedMergeProperty<Monomial> {

    private MaskContext context;

    public SimpleMonomialAdditionProperty(Monomial p1, Monomial p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        return p1.isSimple() && p2.isSimple() && p1.hasSameVariablePartAs(p2);
    }

    @Override
    public void blendResult(List<Monomial> constructed) {
        constructed.add(simpleSum(p1, p2));
    }

    // No layers sum
    public Monomial simpleSum(Monomial a, Monomial b) {
        // We are guaranteed that both variable part are identical
        BigDecimal bigA = new BigDecimal(a.getNumericValue(), context.getMathContext());
        BigDecimal bigB = new BigDecimal(b.getNumericValue(), context.getMathContext());
        float sumResult = bigA.add(bigB).floatValue();
        return new Monomial(sumResult, a.getVarPart());
    }
}
