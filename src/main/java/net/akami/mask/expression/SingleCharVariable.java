package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;

public class SingleCharVariable implements Variable<SingleCharVariable> {

    private final char var;
    private final MaskContext context;

    public SingleCharVariable(char var, MaskContext context) {
        this.var = var;
        this.context = context == null ? MaskContext.DEFAULT : context;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SingleCharVariable)) return false;
        return var == ((SingleCharVariable) obj).var;
    }

    @Override
    public String getExpression() {
        return String.valueOf(var);
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public int compareTo(SingleCharVariable o) {
        return this.var - o.var;
    }

    public char getVar() {
        return var;
    }

    public MaskContext getContext() {
        return context;
    }
}
