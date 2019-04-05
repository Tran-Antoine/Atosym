package net.akami.mask.operation;

public interface InputModifier extends CalculationAffection {

    String[] modify(String... input);
}
