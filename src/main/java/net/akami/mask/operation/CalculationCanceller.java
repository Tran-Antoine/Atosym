package net.akami.mask.operation;

public interface CalculationCanceller extends CalculationAffection {

    String resultIfCancelled(String... input);
}
