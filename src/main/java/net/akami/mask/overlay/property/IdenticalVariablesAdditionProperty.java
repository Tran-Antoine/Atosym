package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IdenticalVariablesAdditionProperty implements OverallMergeProperty<Monomial, List<Monomial>, NullPacket> {

    private MaskContext context;

    public IdenticalVariablesAdditionProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public Optional<NullPacket> isApplicable(Monomial m1, Monomial m2) {
        return m1.getVarPart().equals(m2.getVarPart()) ? Optional.of(NullPacket.PACKET) : Optional.empty();
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2, NullPacket packet) {
        BigDecimal bigA = new BigDecimal(m1.getNumericValue(), context.getMathContext());
        BigDecimal bigB = new BigDecimal(m2.getNumericValue(), context.getMathContext());
        float sumResult = bigA.add(bigB).floatValue();

        return Collections.singletonList(new Monomial(sumResult, m1.getVarPart()));
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }
}
