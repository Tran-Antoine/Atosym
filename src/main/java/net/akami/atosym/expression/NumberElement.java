package net.akami.atosym.expression;

import java.util.Collections;

/**
 * A shortcut to instantiate monomials that have no literal part
 */
public class NumberElement extends Monomial {

    public static final NumberElement SUM_SUB_NULL_FACTOR = new NumberElement(0);
    public static final NumberElement MULT_DIV_NULL_FACTOR = new NumberElement(1);

    public NumberElement(float number) {
        super(number, Collections.emptyList());
    }
}
