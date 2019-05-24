package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;

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
}
