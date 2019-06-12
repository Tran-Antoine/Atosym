package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.handler.Adder;
import net.akami.mask.merge.property.*;

import java.util.Arrays;
import java.util.List;

public class MonomialAdditionMerge implements SequencedMerge<Monomial> {

    private MaskContext context;

    public MonomialAdditionMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<ElementSequencedMergeProperty<Monomial>> generateElementProperties(Monomial p1, Monomial p2) {
        return Arrays.asList(
            new SimpleMonomialAdditionProperty(p1, p2, context),
            new CosineSinusSquaredProperty(p1, p2),
            new CommonDenominatorAdditionProperty(p1, p2, context),
            new IdenticalVariablePartProperty(p1, p2, context)
        );
    }

    public class SimpleMonomialAdditionProperty extends ElementSequencedMergeProperty<Monomial> {

        private MaskContext context;

        protected SimpleMonomialAdditionProperty(Monomial p1, Monomial p2, MaskContext context) {
            super(p1, p2, false);
            this.context = context;
        }

        @Override
        public boolean isSuitable() {
            return p1.isSimple() && p2.isSimple() && p1.hasSameVariablePartAs(p2);
        }

        @Override
        public void blendResult(List<Monomial> constructed) {
            Adder adder = context.getBinaryOperation(Adder.class);
            constructed.add(adder.simpleSum(p1, p2));
        }
    }
}
