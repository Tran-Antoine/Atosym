package net.akami.atosym.function;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.FairMerge;
import net.akami.atosym.merge.MonomialMultiplicationMerge;
import net.akami.atosym.merge.SequencedMerge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MultOperator extends BinaryOperator {

    private MaskContext context;

    public MultOperator(MaskContext context) {
        super("mult", "*", "");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        List<MathObject> objects = new ArrayList<>();
        objects.add(a);
        objects.add(b);

        SequencedMerge<MathObject> merge = new MonomialMultiplicationMerge(context);
        objects = merge.merge(objects, objects, true);

        if(objects.size() == 1) {
            return objects.get(0);
        }

        return new MultMathObject(objects);
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
                rawResult.addBranch(multBehavior.merge(a, b, false));
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
