package net.akami.mask.expression;

import java.util.List;
import java.util.Optional;

public interface Variable {

    String getExpression();

    List<Monomial> getElements();

    Optional<Float> getFinalExponent();
}
