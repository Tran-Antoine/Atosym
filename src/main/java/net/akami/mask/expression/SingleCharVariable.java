package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;
import net.akami.mask.overlay.ExpressionOverlay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SingleCharVariable implements Variable {

    private final char var;
    private final MaskContext context;

    public SingleCharVariable(char var, MaskContext context) {
        this.var = var;
        this.context = context == null ? MaskContext.DEFAULT : context;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Variable)) return false;
        return getExpression().equals(((Variable) obj).getExpression());
    }

    @Override
    public String getExpression() {
        return String.valueOf(var);
    }

    @Override
    public String toString() {
        return getExpression();
    }

    public char getVar() {
        return var;
    }

    public MaskContext getContext() {
        return context;
    }

    @Override
    public List<Monomial> getElements() {
        return Collections.singletonList(new Monomial(var, context));
    }

    @Override
    public Optional<Float> getFinalExponent() {
        return Optional.of(1f);
    }

    @Override
    public boolean isFraction() {
        return false;
    }

    @Override
    public List<Monomial> uncover(int amount) {
        throw new UnsupportedOperationException("Cannot uncover a single char variable");
    }

    @Override
    public ExpressionOverlay getOverlay(int i) {
        throw new UnsupportedOperationException("Simple variables don't have overlays");
    }

    @Override
    public List<Monomial> getNumerator() {
        return getElements();
    }

    @Override
    public List<Monomial> getDenominator() {
        return Collections.singletonList(NumberElement.MULT_DIV_NULL_FACTOR);
    }

    @Override
    public int getOverlaysSize() {
        return 0;
    }

    @Override
    public int getElementsSize() {
        return 1;
    }

    @Override
    public List<ExpressionOverlay> getOverlaysSection(int start, int end) {
        return Collections.emptyList();
    }
}
