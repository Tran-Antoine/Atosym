package net.akami.mask.operation;

public interface CalculationAffection {

    boolean appliesTo(String... input);
    int priorityLevel();
}
