package net.akami.mask.encapsulator.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.IrreducibleVarPart;

import java.util.ArrayList;
import java.util.List;

public class PowMultiplicationProperty implements EncapsulatorMergeProperty {

    private MaskContext context;

    public PowMultiplicationProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isApplicableFor(IrreducibleVarPart v1, IrreducibleVarPart v2) {

        List<ExpressionEncapsulator> l1 = v1.getLayers();
        List<ExpressionEncapsulator> l2 = v2.getLayers();

        if(l1.size() == 0 || l2.size() == 0) return false;

        List<ExpressionEncapsulator> copy1 = new ArrayList<>(l1);
        copy1.remove(copy1.size()-1);
        List<ExpressionEncapsulator> copy2 = new ArrayList<>(l1);
        copy2.remove(copy2.size()-1);

        if(!copy1.equals(copy2)) return false;

        return l1.get(l1.size()-1) instanceof Expression && l2.get(l2.size()-1) instanceof Expression;
    }

    @Override
    public Expression merge(Expression a, Expression b) {

        /*List<ExpressionEncapsulator> l1 = v1.getLayers();
        List<ExpressionEncapsulator> l2 = v2.getLayers();

        List<ExpressionEncapsulator> copy = new ArrayList<>(l1);
        copy.remove(copy.size()-1);

        Expression last1 = (Expression) l1.get(l1.size()-1);
        Expression last2 = (Expression) l2.get(l2.size()-1);

        Adder operator = context.getBinaryOperation(Adder.class);
        copy.add(ExponentEncapsulator.fromExpression(operator.operate(last1, last2)));
        return new IrreducibleVarPart(v1.getElements(), copy);*/
        return null;
    }
}
