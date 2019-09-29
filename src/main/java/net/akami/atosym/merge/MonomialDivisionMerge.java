package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.OverallMergeProperty;

import java.util.Arrays;
import java.util.List;

public class MonomialDivisionMerge implements
        Merge<MathObject, List<MathObject>, OverallMergeProperty<MathObject, List<MathObject>>> {

    private MaskContext context;

    public MonomialDivisionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<OverallMergeProperty<MathObject, List<MathObject>>> generateOverallProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                //new NumericalDivisionProperty(p1, p2, context),
                //new DivisionOfFractionsProperty(p1, p2, context),
                //new StandardDivisionProperty(p1, p2, context)
        );
    }
}
