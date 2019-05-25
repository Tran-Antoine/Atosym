package net.akami.mask.overlay.property;

import net.akami.mask.expression.Monomial;

import java.util.Optional;

public interface OverallMergeProperty<T, R, D extends MergePacket> extends OverlayMergeProperty {

    Optional<D> isApplicable(T m1, T m2);
    R result(Monomial m1, Monomial m2, D packet);
}
