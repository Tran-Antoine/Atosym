package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.merge.MonomialAdditionMerge;
import net.akami.mask.merge.MonomialMultiplicationMerge;
import net.akami.mask.utils.VariableUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Multiplier extends BinaryOperationHandler<Expression> {

    public Multiplier(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {

        LOGGER.debug("Operating mult {} * {}", a, b);
        if(a.size() != 0 && a.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");
        if(b.size() != 0 && b.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");

        List<Monomial> elements = new ArrayList<>(a.length()*b.length());

        for(Monomial elemA : a.getElements()) {
            for(Monomial elemB : b.getElements()) {
                elements.add(simpleMult(elemA, elemB));
            }
        }

        List<Monomial> mergedElements = new MonomialAdditionMerge(context).merge(elements, elements, true);
        return new Expression(mergedElements);
    }

    public Monomial simpleMult(Monomial a, Monomial b) {
        if(a.getVarPart().isSimple() && b.getVarPart().isSimple())
            return noLayersMult(a, b);

        return complexMult(a, b);
    }

    public Monomial noLayersMult(Monomial a, Monomial b) {
        BigDecimal bigA = new BigDecimal(a.getNumericValue(), context.getMathContext());
        BigDecimal bigB = new BigDecimal(b.getNumericValue(), context.getMathContext());
        float numResult = bigA.multiply(bigB).floatValue();
        List<Variable> numVariables = VariableUtils.combine(a.getVarPart(), b.getVarPart(), context, false);
        return new Monomial(numResult, numVariables);
    }

    private Monomial complexMult(Monomial a, Monomial b) {
        MonomialMultiplicationMerge merge = new MonomialMultiplicationMerge(a, b, propertyManager.getProperties());
        float floatMult = numericMult(a.getNumericValue(), b.getNumericValue());
        return new Monomial(floatMult, merge.merge());
    }

    public float numericMult(float a, float b) {
        BigDecimal bigA = new BigDecimal(a, context.getMathContext());
        BigDecimal bigB = new BigDecimal(b, context.getMathContext());
        return bigA.multiply(bigB).floatValue();
    }
}
