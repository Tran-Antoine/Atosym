package net.akami.mask.merge;

import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Adder;

import java.util.Collections;
import java.util.Set;

public class VariableCombination implements MergeBehavior<Variable> {

    @Override
    public boolean isMergeable(Variable a, Variable b) {

        boolean equalVars = a.getVar() == b.getVar();
        boolean noFunction = a.getFunction() == b.getFunction();

        if(!equalVars) return false;
        if(noFunction) return true;

        if(a.getFunction() == null || b.getFunction() == null)
            return false;

        return a.getFunction().equals(b.getFunction());
    }

    @Override
    public Variable mergeElement(Variable a, Variable b) {
        Adder operator = a.getContext().getBinaryOperation(Adder.class);

        Expression newExponent = operator.simpleSum(a.getExponent(), b.getExponent());
        return new Variable(a.getVar(), newExponent, a.getFunction(), a.getContext());
    }

    @Override
    public Set<Class<? extends Variable>> getHandledTypes() {
        return Collections.singleton(Variable.class);
    }
}
