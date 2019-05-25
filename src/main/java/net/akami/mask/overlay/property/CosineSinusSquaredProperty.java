package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.PairNullifying;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.property.CosineSinusSquaredProperty.CosineSinusSquaredPacket;
import net.akami.mask.utils.VariableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CosineSinusSquaredProperty implements OverallMergeProperty<Monomial, List<Monomial>, CosineSinusSquaredPacket> {

    private MaskContext context;

    public CosineSinusSquaredProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public Optional<CosineSinusSquaredPacket> isApplicable(Monomial m1, Monomial m2) {

        if(m1.getNumericValue() != m2.getNumericValue()) return Optional.empty();

        List<Variable> l1 = m1.getVarPart().getVariables();
        List<Variable> l2 = m2.getVarPart().getVariables();

        MergeManager mergeManager = context.getMergeManager();
        MergeBehavior<Object> nullification = mergeManager.getByType(PairNullifying.class);
        List<Variable> differences = mergeManager.secureMerge(l1, l2, nullification, false, VariableComparator.COMPARATOR);
        if(differences.size() != 2) return Optional.empty();

        Variable v1 = differences.get(0);
        Variable v2 = differences.get(1);

        if(!v1.getElements().equals(v2.getElements())) return Optional.empty();
        if(v1.getOverlaysSize() < 2 || v2.getOverlaysSize() < 2) return Optional.empty();
        if(!v1.getOverlaysSection(0, -2).equals(v2.getOverlaysSection(0, -2))) return Optional.empty();
        if(!v1.getOverlay(-1).equals(v2.getOverlay(-1))) return Optional.empty();

        ExpressionOverlay last = v1.getOverlay(-1);
        if(!(last instanceof ExponentOverlay) || !last.equals(ExponentOverlay.SQUARED)) return Optional.empty();

        int cosLeft = 1;
        int sinLeft = 1;

        if(v1.getOverlay(-2) instanceof SinusFunction)  sinLeft--;
        if(v1.getOverlay(-2) instanceof CosineFunction) cosLeft--;
        if(v2.getOverlay(-2) instanceof SinusFunction)  sinLeft--;
        if(v2.getOverlay(-2) instanceof CosineFunction) cosLeft--;

        if(cosLeft == 0 && sinLeft == 0){
            CosineSinusSquaredPacket packet = new CosineSinusSquaredPacket();
            packet.v1 = v1;
            packet.v2 = v2;
            return Optional.of(packet);
        }
        return Optional.empty();
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2, CosineSinusSquaredPacket packet) {
        // The property makes that we don't sum the two values
        float sumResult = m1.getNumericValue();

        List<Variable> copy = new ArrayList<>(m1.getVarPart().getVariables());

        Variable toRemove;
        if(copy.contains(packet.v1)) toRemove = packet.v1;
        else toRemove = packet.v2;

        copy.remove(toRemove);
        copy.sort(VariableComparator.COMPARATOR);
        return Collections.singletonList(new Monomial(sumResult, new VariablePart(copy)));
    }

    @Override
    public boolean requiresStartingOver() {
        return true;
    }

    protected static class CosineSinusSquaredPacket implements MergePacket {

        private Variable v1;
        private Variable v2;

    }
}
