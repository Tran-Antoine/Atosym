package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.overlay.ExponentEncapsulator;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.property.MergePropertyManager;
import net.akami.mask.handler.Adder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VariableCombination implements MergeBehavior<Variable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableCombination.class);
    private MaskContext context;

    public VariableCombination(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isMergeable(Variable a, Variable b) {

        if (a instanceof SingleCharVariable && b instanceof SingleCharVariable) {
            return ((SingleCharVariable) a).getVar() == ((SingleCharVariable) b).getVar();
        }

        if(a instanceof SingleCharVariable) {
            return simpleCompatibleWithComplex((SingleCharVariable) a, (ComplexVariable) b);
        }

        if(b instanceof SingleCharVariable) {
            return simpleCompatibleWithComplex((SingleCharVariable) b, (ComplexVariable) a);
        }

        ComplexVariable complexA = (ComplexVariable) a;
        ComplexVariable complexB = (ComplexVariable) b;
        List<ExpressionOverlay> aOverlays = complexA.getOverlaysFraction(-1);
        List<ExpressionOverlay> bOverlays = complexB.getOverlaysFraction(-1);

        return complexA.elementsEqual(complexB) && aOverlays.equals(bOverlays);
    }

    private boolean simpleCompatibleWithComplex(SingleCharVariable a, ComplexVariable b) {
        if (b.elementsLength() != 1 || !(b.getLayer(-1) instanceof ExponentEncapsulator))
            return false;

        Monomial first = b.getElement(0);
        if (b.elementsLength() != 1 || first.getVarPart().isSimple())
            return false;

        SingleCharVariable firstSingle = (SingleCharVariable) first.getVarPart().get(0);
        return a.getVar() == firstSingle.getVar();
    }

    @Override
    public Variable mergeElement(Variable a, Variable b) {

    }

    private ComplexVariable identicalVariables(ComplexVariable a) {
        List<ExpressionOverlay> layers = new ArrayList<>(a.getOverlays());
        layers.add(new ExponentEncapsulator(2));
        return new ComplexVariable(a.getElements(), layers);
    }
    
    @Override
    public Set<Class<? extends Variable>> getHandledTypes() {
        return Collections.singleton(Variable.class);
    }
}
