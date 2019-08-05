package net.akami.atosym.handler;

public interface PostCalculationActionable<T> {

    void postCalculation(T result, T... input);
}
