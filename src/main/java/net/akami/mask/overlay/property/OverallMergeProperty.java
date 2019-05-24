package net.akami.mask.overlay.property;

import net.akami.mask.expression.Monomial;

import java.util.List;

public interface OverallMergeProperty extends OverlayMergeProperty {

    boolean isApplicable(Monomial m1, Monomial m2);
    List<Monomial> result(Monomial m1, Monomial m2);
}
