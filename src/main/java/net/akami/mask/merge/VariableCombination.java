package net.akami.mask.merge;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.encapsulator.MergePropertyManager;
import net.akami.mask.expression.ComposedVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.SimpleVariable;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Adder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VariableCombination implements MergeBehavior<Variable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableCombination.class);

    private MergePropertyManager propertyManager;

    @Override
    public boolean isMergeable(Variable a, Variable b) {

        if(!a.getClass().equals(b.getClass())) return false;

        if(a instanceof ComposedVariable || b instanceof ComposedVariable) {
            ComposedVariable compA = (ComposedVariable) a;
            ComposedVariable compB = (ComposedVariable) b;
            return a.equals(b) || propertyManager.getComposedResult(compA, compB).isPresent();
        }

        SimpleVariable simpleA = (SimpleVariable) a;
        SimpleVariable simpleB = (SimpleVariable) b;
        boolean equalVars = simpleA.getVar() == simpleB.getVar();
        boolean noFunction = simpleA.getFunction() == simpleB.getFunction();

        if(!equalVars) return false;
        if(noFunction) return true;

        if(simpleA.getFunction() == null || simpleB.getFunction() == null)
            return false;

        return simpleA.getFunction().equals(simpleB.getFunction());
    }

    @Override
    public Variable mergeElement(Variable a, Variable b) {

        if(a instanceof ComposedVariable || b instanceof ComposedVariable) {
            if(propertyManager == null) {
               LOGGER.error("UNDEFINED PROPERTY MANAGER");
               throw new IllegalStateException();
            }

            if(a.equals(b)) {
                return identicalVariables((ComposedVariable) a);
            }

            return propertyManager.getComposedResult((ComposedVariable) a, (ComposedVariable) b).get();
        }

        SimpleVariable simpleA = (SimpleVariable) a;
        SimpleVariable simpleB = (SimpleVariable) b;

        Adder operator = simpleA.getContext().getBinaryOperation(Adder.class);

        Expression newExponent = operator.simpleSum(simpleA.getExponent(), simpleB.getExponent());
        return new SimpleVariable(simpleA.getVar(), newExponent, simpleA.getFunction(), simpleA.getContext());
    }

    private ComposedVariable identicalVariables(ComposedVariable a) {
        List<ExpressionEncapsulator> layers = new ArrayList<>(a.getLayers());
        layers.add(Expression.of(2));
        return new ComposedVariable(a.getElements(), layers);
    }

    public MergePropertyManager getPropertyManager() {
        return propertyManager;
    }

    public void setPropertyManager(MergePropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public Set<Class<? extends Variable>> getHandledTypes() {
        return Collections.singleton(Variable.class);
    }
}
