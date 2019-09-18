package net.akami.atosym.merge.property;

import net.akami.atosym.core.MaskContext;

import java.math.BigDecimal;
import java.util.List;

public class IdenticalVariablePartProperty extends ElementSequencedMergeProperty<Monomial> {

    private MaskContext context;

    public IdenticalVariablePartProperty(Monomial m1, Monomial m2, MaskContext context) {
        super(m1, m2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        return p1.getVarPart().equals(p2.getVarPart());
    }

    @Override
    public void blendResult(List<Monomial> constructed) {
        BigDecimal bigA = new BigDecimal(p1.getNumericValue(), context.getMathContext());
        BigDecimal bigB = new BigDecimal(p2.getNumericValue(), context.getMathContext());
        float sumResult = bigA.add(bigB).floatValue();

        constructed.add(new Monomial(sumResult, p1.getVarPart()));
    }
}
