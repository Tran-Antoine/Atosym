package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.IntricateVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Adder;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;

import java.util.ArrayList;
import java.util.List;

public class BaseEquivalenceMultProperty extends ElementSequencedMergeProperty<Variable> {

    private MaskContext context;

    private Expression exponent1;
    private Expression exponent2;
    private List<Monomial> elements;
    private List<ExpressionOverlay> otherOverlays;

    public BaseEquivalenceMultProperty(Variable v1, Variable v2, MaskContext context) {
        super(v1, v2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {

        if(!p1.getElements().equals(p2.getElements())) return false;

        int end1 = -1;
        int end2 = -1;

        if(p1.getOverlaysSize() != 0 && p1.getOverlay(-1) instanceof ExponentOverlay) end1--;
        if(p2.getOverlaysSize() != 0 && p2.getOverlay(-1) instanceof ExponentOverlay) end2--;

        List<ExpressionOverlay> overlaysFraction1 = p1.getOverlaysSection(0, end1);
        List<ExpressionOverlay> overlaysFraction2 = p2.getOverlaysSection(0, end2);

        if(overlaysFraction1.equals(overlaysFraction2)) {
            Expression oneExponent = ExponentOverlay.NULL_FACTOR;
            // overlaysFraction1 equals overlaysFraction2, so it doesn't matter which of the two is chosen
            otherOverlays = overlaysFraction1;
            elements = p1.getElements();
            exponent1 = end1 == -1 ? oneExponent : (Expression) p1.getOverlay(-1);
            exponent2 = end2 == -1 ? oneExponent : (Expression) p2.getOverlay(-1);
            return true;
        }
        return false;
    }

    @Override
    public void blendResult(List<Variable> constructed) {
        List<Monomial> newElements = elements;
        Expression newExponent = context.getBinaryOperation(Adder.class).operate(exponent1, exponent2);
        List<ExpressionOverlay> overlays = new ArrayList<>(otherOverlays);
        // The list is modifiable because created using the getFractionOverlay() method
        overlays.add(ExponentOverlay.fromExpression(newExponent));
        constructed.add(new IntricateVariable(newElements, overlays));
    }
}
