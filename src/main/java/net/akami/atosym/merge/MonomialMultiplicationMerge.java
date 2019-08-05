package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Monomial;
import net.akami.atosym.expression.Variable;
import net.akami.atosym.handler.Multiplier;
import net.akami.atosym.merge.property.FairOverallMergeProperty;
import net.akami.atosym.utils.VariableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonomialMultiplicationMerge implements FairMerge<Monomial, FairOverallMergeProperty<Monomial>>{

    private MaskContext context;

    public MonomialMultiplicationMerge(MaskContext context) {
        this.context = context;
    }

    @Override
    public List<FairOverallMergeProperty<Monomial>> generateOverallProperties(Monomial p1, Monomial p2) {
        return Collections.singletonList(
                new MultiplicationMergeProperty(p1, p2)
        );
    }

    /**
     * Property stating that every monomial can be multiplied successfully with any other. <br>
     * In other words, there is only one multiplication property which multiplies the coefficients of both
     * monomials, then use another merge behavior to get the resulting variables from the two monomials.
     * A unique monomial is thus created, which will be added into the list.
     */
    public class MultiplicationMergeProperty extends FairOverallMergeProperty<Monomial> {

        protected MultiplicationMergeProperty(Monomial p1, Monomial p2) {
            super(p1, p2, false);
        }

        /**
         * Multiplications can always be performed, resulting in a single result
         * @return true
         */
        @Override
        public boolean isSuitable() {
            return true;
        }

        @Override
        public Monomial computeResult() {
            Multiplier multiplier = context.getBinaryOperation(Multiplier.class);
            float floatResult = multiplier.mult(p1.getNumericValue(), p2.getNumericValue());

            SequencedMerge<Variable> merge = new VariableCombination(context);
            List<Variable> vars1 = new ArrayList<>(p1.getVarPart().getVariables());
            List<Variable> vars2 = new ArrayList<>(p2.getVarPart().getVariables());

            List<Variable> finalVars = merge.merge(vars1, vars2, false);
            finalVars.sort(VariableComparator.COMPARATOR);

            return new Monomial(floatResult, finalVars);
        }
    }
}
