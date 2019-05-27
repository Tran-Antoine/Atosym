package net.akami.mask.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MaskImageCalculatorTest {

    @Test
    public void imageCalculationTest() {

        Map<Character, String> map = new HashMap<>();
        map.put('x', "1");

        MaskOperatorHandler handler = new MaskOperatorHandler();
        Mask in = new Mask("5x+3+x");
        Mask out = new Mask();
        handler.compute(MaskImageCalculator.class, in, out, map);
        assertThat(out.getExpression()).isEqualTo("9.0");
    }
}
