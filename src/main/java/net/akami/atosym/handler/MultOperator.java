package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;

import java.math.BigDecimal;

public class MultOperator extends BinaryOperator {

    private MaskContext context;

    public MultOperator(MaskContext context) {
        super("mult");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        /*List<Monomial> aMonomials = a.getElements();
        List<Monomial> bMonomials = b.getElements();

        LOGGER.debug("Operating mult {} * {}", a, b);
        if(a.length() != 0 && aMonomials.get(0) instanceof FunctionSign) {
            return functionOperation(a, b);
        }

        if(b.length() != 0 && bMonomials.get(0) instanceof FunctionSign) {
            return functionOperation(b, a);
        }

        List<Monomial> reducedResult = resolveMult(aMonomials, bMonomials);
        return new Expression(reducedResult);*/
        return null;
    }

    /*private Expression functionOperation(Expression bindingExpression, Expression target) {
        char binding = bindingExpression.get(0).getExpression().charAt(0);
        Optional<MathOperator> optional = context.getFunctionByBinding(binding);
        if(optional.isPresent()) return optional.get().rawOperate(target);
        else throw new IllegalStateException("Unknown binding");
    }

    private List<Monomial> resolveMult(List<Monomial> aMonomials, List<Monomial> bMonomials) {

        int initialCapacity = aMonomials.size() * bMonomials.size();

        FairMerge<Monomial, FairOverallMergeProperty<Monomial>> multBehavior = new MonomialMultiplicationMerge(context);
        SequencedMerge<Monomial> additionBehavior = new MonomialAdditionMerge(context);

        List<Monomial> rawResult = new ArrayList<>(initialCapacity);

        for(Monomial a : aMonomials) {
            for(Monomial b : bMonomials) {
                rawResult.add(multBehavior.merge(a, b, false));
            }
        }
        List<Monomial> reducedResult = additionBehavior.merge(rawResult, rawResult, true);
        Collections.sort(reducedResult);
        return reducedResult;
    }
    */
    public float mult(float a, float b) {
        BigDecimal bigA = new BigDecimal(a, context.getMathContext());
        BigDecimal bigB = new BigDecimal(b, context.getMathContext());
        return bigA.multiply(bigB).floatValue();
    }
}
