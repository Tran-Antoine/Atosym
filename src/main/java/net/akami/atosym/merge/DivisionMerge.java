package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.merge.property.division.NumericDivisionProperty;

import java.util.Arrays;
import java.util.List;

public class DivisionMerge implements FairMerge<MathObject, FairOverallMergeProperty<MathObject>> {

    private MaskContext context;

    public DivisionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<FairOverallMergeProperty<MathObject>> generateOverallProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericDivisionProperty(p1, p2, context)
                //new NumericalDivisionProperty(p1, p2, context),
                //new DivisionOfFractionsProperty(p1, p2, context),
                //new StandardDivisionProperty(p1, p2, context)
        );
    }
}
