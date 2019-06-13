package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.merge.property.*;

import java.util.Arrays;
import java.util.List;

public class MonomialDivisionMerge implements Merge<Monomial, List<Monomial>, OverallMergeProperty<Monomial, List<Monomial>>> {

    private MaskContext context;

    public MonomialDivisionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<Monomial> merge(Monomial p1, Monomial p2, boolean selfMerge) {
        for(OverallMergeProperty<Monomial, List<Monomial>> property : generateOverallProperties(p1, p2)) {
            if(property.isSuitable()) return property.computeResult();
        }
        throw new IllegalStateException("Unable to find a property matching the two monomials");
    }

    @Override
    public List<OverallMergeProperty<Monomial, List<Monomial>>> generateOverallProperties(Monomial p1, Monomial p2) {
        return Arrays.asList(
                new NumericalDivisionProperty(p1, p2, context),
                new DivisionOfFractionsProperty(p1, p2, context),
                new StandardDivisionProperty(p1, p2, context)
        );
    }
}
