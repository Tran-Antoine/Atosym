package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.OverallMergeProperty;
import java.util.Collections;
import java.util.List;

public class DivisionMerge implements BiSequencedMerge<MathObject> {

    private MaskContext context;

    public DivisionMerge(MaskContext context) {
        this.context = context;
    }
}
