package net.akami.mask.handler;

public interface PostCalculationActionable<T> {

    void postCalculation(T result, T... input);
}
