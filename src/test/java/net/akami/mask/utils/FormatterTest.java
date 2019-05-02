package net.akami.mask.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class FormatterTest {

    @Test
    public void trigonometryTest() {
        StringModifier forCalculations = FormatterFactory::formatForCalculations;
        StringModifier forVisual = FormatterFactory::formatForVisual;
        assertModification(forCalculations, "5sin(25)+3sin(20)+4x", "5*((25)@)+3*((20)@)+4*x");
        assertModification(forVisual, "5*((25)@)+3*((20)@)+4*x", "5sin(25)+3sin(20)+4x");
        assertModification(forVisual, MathUtils.mult("5","((x)#)"), "5cos(x)");
    }

    @Test
    public void coefficientTest() {
        StringModifier addCoefficient = FormatterFactory::addAllCoefficients;
        assertModification(addCoefficient, "x", "1x");
        assertModification(addCoefficient, "xy", "1xy");
    }

    private void assertModification(StringModifier modifier, String input, String result) {
        assertThat(modifier.modify(input)).isEqualTo(result);
    }

    @FunctionalInterface
    private interface StringModifier {
        String modify(String origin);
    }
}
