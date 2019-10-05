package net.akami.atosym.core;

import net.akami.atosym.alteration.CalculationAlteration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaskContextTest {

    @Test
    public void sortTest() {

        List<CalculationAlteration> list = new ArrayList<>();
        testWithLevel(list, 3.0f);
        testWithLevel(list, 5.0f);
        testWithLevel(list, 1.0f);

        Collections.sort(list);
        String s = "";
        for(CalculationAlteration alteration : list) {
            s += String.valueOf(alteration.priorityLevel());
        }
        Assertions.assertThat(s).isEqualTo("5.03.01.0");
    }

    private void testWithLevel(List<CalculationAlteration> list, float level) {
        list.add(new CalculationAlteration<String>() {
            @Override
            public boolean appliesTo(List<String> input) {
                return false;
            }

            @Override
            public float priorityLevel() {
                return level;
            }
        });
    }
}
