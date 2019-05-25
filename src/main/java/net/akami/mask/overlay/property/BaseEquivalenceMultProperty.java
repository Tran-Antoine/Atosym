package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.property.BaseEquivalenceMultProperty.BaseEquivalencePacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseEquivalenceMultProperty implements DetailedMergeProperty<BaseEquivalencePacket> {

    private MaskContext context;

    public BaseEquivalenceMultProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public Optional<BaseEquivalencePacket> isApplicableFor(Variable v1, Variable v2) {

        if(!v1.getElements().equals(v2.getElements())) return Optional.empty();

        int end1 = -1;
        int end2 = -1;

        if(v1.getOverlaysSize() != 0 && v1.getOverlay(-1) instanceof ExponentOverlay) end1--;
        if(v2.getOverlaysSize() != 0 && v2.getOverlay(-1) instanceof ExponentOverlay) end2--;

        List<ExpressionOverlay> overlaysFraction1 = v1.getOverlaysSection(0, end1);
        List<ExpressionOverlay> overlaysFraction2 = v2.getOverlaysSection(0, end2);

        if(overlaysFraction1.equals(overlaysFraction2)) {
            BaseEquivalencePacket packet = new BaseEquivalencePacket();
            Expression oneExponent = Expression.of(1);
            // overlaysFraction1 equals overlaysFraction2, so doesn't matter which of the two is chosen
            packet.otherOverlays = overlaysFraction1;
            packet.elements = v1.getElements();
            packet.exponent1 = end1 == -1 ? oneExponent : (ExponentOverlay) v1.getOverlay(-1);
            packet.exponent2 = end2 == -1 ? oneExponent : (ExponentOverlay) v2.getOverlay(-1);
            return Optional.of(packet);
        }
        return null;
    }

    @Override
    public void merge(Variable v1, Variable v2, List<Variable> allVars, BaseEquivalencePacket packet) {
        List<Monomial> newElements = packet.elements;
        Expression newExponent = context.getBinaryOperation(Adder.class).operate(packet.exponent1, packet.exponent2);
        List<ExpressionOverlay> overlays = new ArrayList<>(packet.otherOverlays);
        // The list is modifiable because created using the getFractionOverlay() method
        overlays.add(ExponentOverlay.fromExpression(newExponent));
        allVars.add(new ComplexVariable(newElements, overlays));
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }

    protected static class BaseEquivalencePacket implements MergePacket {

        private Expression exponent1;
        private Expression exponent2;

        private List<Monomial> elements;
        private List<ExpressionOverlay> otherOverlays;
    }
}
