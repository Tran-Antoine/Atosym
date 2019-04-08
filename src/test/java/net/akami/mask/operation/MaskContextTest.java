package net.akami.mask.operation;

import net.akami.mask.handler.Adder;
import org.junit.Test;

public class MaskContextTest {

    @Test
    public void getAffectionsErrorTest() {
        MaskContext context = new MaskContext();

        context.addHandler(new Adder());

        context.getAffections(CalculationAffection.class);
    }
}
