package net.akami.mask.expression;

import java.util.Collections;

public class NumberElement extends Monomial {

    public NumberElement(float number) {
        super(number, Collections.emptyList());
    }
}
