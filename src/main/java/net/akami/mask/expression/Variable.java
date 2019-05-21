package net.akami.mask.expression;

public interface Variable<T extends Variable<T>> extends Comparable<T>{

    String getExpression();
}
