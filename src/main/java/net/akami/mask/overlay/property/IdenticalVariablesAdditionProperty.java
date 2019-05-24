package net.akami.mask.overlay.property;

import net.akami.mask.expression.Monomial;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class IdenticalVariablesAdditionProperty implements OverallMergeProperty {
    @Override
    public boolean isApplicable(Monomial m1, Monomial m2) {
        return m1.getVarPart().equals(m2.getVarPart());
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2) {
        BigDecimal bigA = new BigDecimal(m1.getNumericValue());
        BigDecimal bigB = new BigDecimal(m2.getNumericValue());
        float sumResult = bigA.add(bigB).floatValue();

        return Collections.singletonList(new Monomial(sumResult, m1.getVarPart()));
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }
}
