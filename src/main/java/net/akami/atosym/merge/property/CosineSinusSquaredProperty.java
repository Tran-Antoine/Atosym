package net.akami.atosym.merge.property;

import net.akami.atosym.function.CosineOperator;
import net.akami.atosym.function.SineOperator;
import net.akami.atosym.merge.PairEraser;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.overlay.ExponentOverlay;
import net.akami.atosym.overlay.ExpressionOverlay;
import net.akami.atosym.utils.VariableComparator;

import java.util.ArrayList;
import java.util.List;

public class CosineSinusSquaredProperty extends ElementSequencedMergeProperty<Monomial> {

    private Variable v1;
    private Variable v2;

    public CosineSinusSquaredProperty(Monomial m1, Monomial m2) {
        super(m1, m2, true);
    }

    @Override
    public boolean isSuitable() {

        // The coefficients must be the same. The property cannot be applied to "2cos^2(x) + 3sin^2(x)" for instance
        if(p1.getNumericValue() != p2.getNumericValue()) return false;
        List<Variable> l1 = p1.getVarPart().getVariables();
        List<Variable> l2 = p2.getVarPart().getVariables();

        SequencedMerge<Variable> nullification = new PairEraser<>();
        List<Variable> differences = nullification.subtypeMerge(l1, l2, false);

        if(differences.size() != 2) return false;

        Variable v1 = differences.get(0);
        Variable v2 = differences.get(1);

        return containsProperty(v1, v2);
    }

    private boolean containsProperty(Variable v1, Variable v2) {

        if(!v1.getElements().equals(v2.getElements())) return false;
        if(v1.getOverlaysSize() < 2 || v2.getOverlaysSize() < 2) return false;
        if(!v1.getOverlaysSection(0, -3).equals(v2.getOverlaysSection(0, -3))) return false;
        if(!v1.getOverlay(-1).equals(v2.getOverlay(-1))) return false;

        ExpressionOverlay last = v1.getOverlay(-1);
        if(!(last instanceof ExponentOverlay) || !last.equals(ExponentOverlay.SQUARED)) return false;

        int cosLeft = 1;
        int sinLeft = 1;

        if(v1.getOverlay(-2) instanceof SineOperator)  sinLeft--;
        if(v1.getOverlay(-2) instanceof CosineOperator) cosLeft--;
        if(v2.getOverlay(-2) instanceof SineOperator)  sinLeft--;
        if(v2.getOverlay(-2) instanceof CosineOperator) cosLeft--;

        if(cosLeft == 0 && sinLeft == 0){
            this.v1 = v1;
            this.v2 = v2;
            return true;
        }
        return false;
    }

    @Override
    public void blendResult(List<Monomial> constructed) {
        // The property makes that we don't sum the two values. Both numeric value must be equal
        float sumResult = p1.getNumericValue();

        List<Variable> copy = new ArrayList<>(p1.getVarPart().getVariables());

        Variable toRemove;
        if(copy.contains(v1)) toRemove = v1;
        else toRemove = v2;

        copy.remove(toRemove);
        copy.sort(VariableComparator.COMPARATOR);
        constructed.add(new Monomial(sumResult, copy));
    }

}
