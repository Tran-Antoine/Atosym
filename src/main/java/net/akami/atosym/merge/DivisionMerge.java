package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.merge.property.division.DecomposableNumeratorProperty;
import net.akami.atosym.merge.property.division.DefaultDivisionProperty;
import net.akami.atosym.merge.property.division.IdenticalNumAndDenProperty;
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
                new IdenticalNumAndDenProperty(p1, p2),
                new NumericDivisionProperty(p1, p2, context),
                new DecomposableNumeratorProperty(p1, p2, context),
                new DefaultDivisionProperty(p1, p2)
        );
    }
}
