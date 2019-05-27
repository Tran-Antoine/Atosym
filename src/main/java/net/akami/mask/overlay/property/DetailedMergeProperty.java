package net.akami.mask.overlay.property;

import net.akami.mask.expression.Variable;

import java.util.List;
import java.util.Optional;

public interface DetailedMergeProperty<D extends MergePacket> extends OverlayMergeProperty {

    Optional<D> isApplicableFor(Variable v1, Variable v2);
    void merge(Variable v1, Variable v2, List<Variable> allVars, D packet);
}
