package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.FunctionSign;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.merge.OverlayMultiplicationMerge;
import net.akami.mask.overlay.property.BaseEquivalenceMultProperty;
import net.akami.mask.overlay.property.FractionMultProperty;
import net.akami.mask.utils.VariableUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Multiplier extends BinaryOperationHandler<Expression> {

    public Multiplier(MaskContext context) {
        super(context);
        addDefaultProperties();
    }

    private void addDefaultProperties() {
        super.getPropertyManager().addProperty(
                new BaseEquivalenceMultProperty(context),
                new FractionMultProperty(context)
        );
    }

    @Override
    public Expression operate(Expression a, Expression b) {

        LOGGER.error("Operating mult {} * {}", a, b);
        if(a.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");
        if(b.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");

        List<Monomial> elements = new ArrayList<>(a.length()*b.length());

        for(Monomial elemA : a.getElements()) {
            for(Monomial elemB : b.getElements()) {
                elements.add(simpleMult(elemA, elemB));
            }
        }

        List<Monomial> mergedElements = context.getMergeManager().merge(elements, Monomial.class);
        return new Expression(mergedElements);
    }

    public Monomial simpleMult(Monomial a, Monomial b) {
        if(a.getVarPart().isSimple() && b.getVarPart().isSimple())
            return noLayersMult(a, b);

        return complexMult(a, b);
    }

    public Monomial noLayersMult(Monomial a, Monomial b) {
        BigDecimal bigA = new BigDecimal(a.getNumericValue());
        BigDecimal bigB = new BigDecimal(b.getNumericValue());
        float numResult = bigA.multiply(bigB).floatValue();
        List<Variable> numVariables = VariableUtils.combine(a.getVarPart(), b.getVarPart(), context);
        return new Monomial(numResult, numVariables);
    }

    private Monomial complexMult(Monomial a, Monomial b) {
        OverlayMultiplicationMerge merge = new OverlayMultiplicationMerge(a, b, propertyManager.getProperties());
        float floatMult = numericMult(a.getNumericValue(), b.getNumericValue());
        return new Monomial(floatMult, merge.merge());
    }

    public float numericMult(float a, float b) {
        BigDecimal bigA = new BigDecimal(a);
        BigDecimal bigB = new BigDecimal(b);
        return bigA.multiply(bigB).floatValue();
    }

    @Override
    public Expression inFormat(Expression origin) {
        return origin;
    }

    @Override
    public Expression outFormat(Expression origin) {
        return origin;
    }

}
