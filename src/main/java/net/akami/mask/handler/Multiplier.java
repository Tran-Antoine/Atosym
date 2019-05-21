package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.overlay.property.PowMultiplicationProperty;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.FunctionSign;
import net.akami.mask.expression.Variable;
import net.akami.mask.merge.MergeManager;
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
        super.getPropertyManager().addProperty(new PowMultiplicationProperty(context));
    }

    @Override
    public Expression operate(Expression a, Expression b) {

        LOGGER.error("Operating mult {} * {}", a, b);
        // TODO : add support for functions
        if(a.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");
        if(b.getElements().get(0) instanceof FunctionSign) throw new RuntimeException("Unsupported yet");

        List<Monomial> elements = new ArrayList<>(a.length()*b.length());

        for(Monomial elemA : a.getElements()) {
            for(Monomial elemB : b.getElements()) {
                elements.add(simpleMult(elemA, elemB));
            }
        }

        List<Monomial> mergedElements = MergeManager.merge(elements, Monomial.class);
        return new Expression(mergedElements);
    }

    public Monomial simpleMult(Monomial a, Monomial b) {

        Monomial numA = null, numB = null;
        Expression denA = null, denB = null;

        /*if(a instanceof SimpleFraction) {
            numA = ((SimpleFraction) a).getNumerator();
            denA = ((SimpleFraction) a).getDenominator();
        } else {
            numA = a;
        }

        if(b instanceof SimpleFraction) {
            numB = ((SimpleFraction) b).getNumerator();
            denB = ((SimpleFraction) b).getDenominator();
        } else {
            numB = b;
        }*/

        Monomial newNumerator;

        if(numA != null && numB != null) {
            newNumerator = simpleMonomialMult(numA, numB);
        } else {
            LOGGER.info("Recursive call");
            newNumerator = simpleMult(numA, numB);
        }

        Expression newDenominator = null;

        if(denA == null && denB == null) return newNumerator;
        if(denA == null) newDenominator = denB;
        if(denB == null) newDenominator = denA;
        if(newDenominator == null) newDenominator = operate(denA, denB);
        // TODO maybe change for a more generic way ?
        if(newDenominator.length() == 1)
            return context.getBinaryOperation(Divider.class).simpleDivision(newNumerator, newDenominator.get(0)).get(0);
        return null; // TODO new SimpleFraction(newNumerator, newDenominator);
    }

    private Monomial simpleMonomialMult(Monomial a, Monomial b) {
         BigDecimal bigA = new BigDecimal(a.getNumericValue());
         BigDecimal bigB = new BigDecimal(b.getNumericValue());
         float numResult = bigA.multiply(bigB).floatValue();
         List<Variable> numVariables = VariableUtils.combine(a.getVarPart(), b.getVarPart(), propertyManager);
         return new Monomial(numResult, numVariables);
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
