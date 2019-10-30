package net.akami.atosym.merge.property.division;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.BiSequencedMerge;
import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;
import net.akami.atosym.merge.property.BiOverallSequencedMergeProperty;

import java.util.Collections;
import java.util.List;

public class IdenticalNumAndDenProperty extends BiOverallSequencedMergeProperty<MathObject> {

    private BiSequencedMerge<MathObject> parent;

    public IdenticalNumAndDenProperty(List<MathObject> p1, List<MathObject> p2, BiSequencedMerge<MathObject> parent) {
        super(p1, p2);
        this.parent = parent;
    }

    @Override
    protected BiListContainer computeResult() {
        return parent.new BiListContainer(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public boolean prepare() {
        return p1.equals(p2);
    }
}
