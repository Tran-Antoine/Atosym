package net.akami.mask.operation;

import net.akami.mask.affection.CalculationAffection;
import net.akami.mask.handler.Adder;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaskContextTest {

    @Test
    public void getAffectionsErrorTest() {
        MaskContext context = new MaskContext();

        context.addHandler(new Adder(context));
        context.getAffections(CalculationAffection.class);
    }

    @Test
    public void sortTest() {

        List<CalculationAffection> list = new ArrayList<>();
        testWithLevel(list, 3.0f);
        testWithLevel(list, 5.0f);
        testWithLevel(list, 1.0f);

        Collections.sort(list);
        String s = "";
        for(CalculationAffection affection : list) {
            s += String.valueOf(affection.priorityLevel());
        }
        Assertions.assertThat(s).isEqualTo("5.03.01.0");
    }

    private void testWithLevel(List<CalculationAffection> list, float level) {
        list.add(new CalculationAffection() {
            @Override
            public boolean appliesTo(String... input) {
                return false;
            }

            @Override
            public float priorityLevel() {
                return level;
            }
        });
    }
}
