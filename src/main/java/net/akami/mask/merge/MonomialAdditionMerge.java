package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.merge.property.*;

import java.util.Arrays;
import java.util.List;

public class MonomialAdditionMerge implements SequencedMerge<Monomial> {

    protected MaskContext context;

    public MonomialAdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<ElementSequencedMergeProperty<Monomial>> generateElementProperties(Monomial p1, Monomial p2) {
        return Arrays.asList(
                new CosineSinusSquaredProperty(p1, p2),
                new CommonDenominatorAdditionProperty(p1, p2, context),
                new IdenticalVariablePartProperty(p1, p2, context)
        );
    }

}
