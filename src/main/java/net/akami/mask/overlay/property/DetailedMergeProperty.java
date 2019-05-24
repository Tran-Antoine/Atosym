package net.akami.mask.overlay.property;

import net.akami.mask.expression.Variable;

import java.util.List;

public interface DetailedMergeProperty extends OverlayMergeProperty {

    boolean isApplicableFor(Variable v1, Variable v2);
    void merge(Variable v1, Variable v2, List<Variable> allVars);
}
