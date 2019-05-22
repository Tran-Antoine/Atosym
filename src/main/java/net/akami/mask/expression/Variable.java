package net.akami.mask.expression;

import java.util.List;

public interface Variable extends Comparable<Variable>{

    String getExpression();

    List<Monomial> getElements();

}
