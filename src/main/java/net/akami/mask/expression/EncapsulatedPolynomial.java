package net.akami.mask.expression;

import net.akami.mask.utils.ExpressionUtils;

import java.util.ArrayList;
import java.util.List;

public class EncapsulatedPolynomial extends ExpressionElement {

    private final List<ExpressionElement> elements;
    private final String expression;

    public EncapsulatedPolynomial(List<ExpressionElement> elements, List<ExpressionEncapsulator> encapsulators) {
        super(encapsulators);
        this.elements = new ArrayList<>(elements);
        this.expression = loadExpression();
    }

    private String loadExpression() {
        return ExpressionUtils.chainElements(this.elements);
    }

    @Override
    public String getRawExpression() {
        return expression;
    }
}
