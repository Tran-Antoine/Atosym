package net.akami.mask.structure;

import net.akami.mask.math.MaskExpression;

import java.util.List;

public class MaskStructure {

    private List<MaskExpression> expressions;

    public MaskStructure(List<MaskExpression> expressions) {
        this.expressions = expressions;
    }

    public List<MaskExpression> getExpressions() {
        return expressions;
    }
}
