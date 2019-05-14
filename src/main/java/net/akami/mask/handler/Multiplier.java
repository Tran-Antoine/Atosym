package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.Mergeable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Multiplier extends BinaryOperationHandler<Expression> {

    public Multiplier(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {

        LOGGER.error("Operating mult {} * {}", a, b);
        // TODO : add support for functions
        if(a.getElements()[0] instanceof FunctionSign) throw new RuntimeException("Unsupported yet");
        if(b.getElements()[0] instanceof FunctionSign) throw new RuntimeException("Unsupported yet");

        List<ExpressionElement> elements = new ArrayList<>(a.length()*b.length());

        for(ExpressionElement elemA : a.getElements()) {

            for(ExpressionElement elemB : b.getElements()) {
                elements.add(simpleMult(elemA, elemB));
            }
        }

        List<ExpressionElement> mergedElements = Mergeable.merge(elements);
        return new Expression(mergedElements.toArray(new ExpressionElement[0]));
    }

    public ExpressionElement simpleMult(ExpressionElement a, ExpressionElement b) {

        Monomial numA, numB, denA = null, denB = null;

        if(a instanceof SimpleFraction) {
            numA = ((SimpleFraction) a).getNumerator();
            denA = ((SimpleFraction) a).getDenominator();
        } else {
            numA = (Monomial) a;
        }
        if(b instanceof SimpleFraction) {
            numB = ((SimpleFraction) b).getNumerator();
            denB = ((SimpleFraction) b).getDenominator();
        } else {
            numB = (Monomial) b;
        }

        Monomial finalNumerator = simpleMonomialMult(numA, numB);

        if(denA == null && denB == null) return finalNumerator;

        denA = denA == null ? new NumberElement(1) : denA;
        denB = denB == null ? new NumberElement(1) : denB;

        Monomial finalDenominator = simpleMonomialMult(denA, denB);

        return new SimpleFraction(finalNumerator, finalDenominator);
     }

     private Monomial simpleMonomialMult(Monomial a, Monomial b) {
         BigDecimal bigA = new BigDecimal(a.getNumericValue());
         BigDecimal bigB = new BigDecimal(b.getNumericValue());
         float numResult = bigA.multiply(bigB).floatValue();
         Variable[] numVariables = Variable.combine(a.getVariables(), b.getVariables());
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
