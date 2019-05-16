package net.akami.mask.expression;

import java.util.Collections;
import java.util.List;

public class NumberElement extends Monomial {

    public NumberElement(float number, List<ExpressionEncapsulator> layers) {
        super(number, Collections.emptyList(), layers);
    }
    public NumberElement(float number) {
        this(number, Collections.emptyList());
    }
}
