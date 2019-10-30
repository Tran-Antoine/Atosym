package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.merge.property.power.BaseExpansionProperty;
import net.akami.atosym.merge.property.power.ChainPowerProperty;
import net.akami.atosym.merge.property.power.DefaultPowerProperty;
import net.akami.atosym.merge.property.power.NumericPowerProperty;

import java.util.Arrays;
import java.util.List;

public class PowerCalculationMerge implements FairMerge<MathObject> {

    private MaskContext context;

    public PowerCalculationMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<FairOverallMergeProperty<MathObject>> generateOverallProperties(MathObject p1, MathObject p2) {
        return Arrays.asList(
                new NumericPowerProperty(p1, p2, context),
                new BaseExpansionProperty(p1, p2, context),
                new ChainPowerProperty(p1, p2, context),
                new DefaultPowerProperty(p1, p2, context)
        );
    }
}
