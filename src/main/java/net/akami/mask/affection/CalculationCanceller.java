package net.akami.mask.affection;

public interface CalculationCanceller extends CalculationAffection {

    String resultIfCancelled(String... input);
}
