package net.akami.mask.overlay;

import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.NumberElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FractionEncapsulatorTest {

    @Test
    public void formatTest() {
        List<Monomial> list = Arrays.asList(
                new NumberElement(4)
        );
        List<Monomial> list2 = Arrays.asList(
                new NumberElement(4),
                new NumberElement(5)
        );

        FractionEncapsulator encapsulator = new FractionEncapsulator(5);
        FractionEncapsulator encapsulator2 = new FractionEncapsulator(list2);
        assertFormat(encapsulator, list, "/5.0");
        assertFormat(encapsulator, list2, "()/5.0");
        assertFormat(encapsulator2, list, "/(4.0+5.0)");
        assertFormat(encapsulator2, list2, "()/(4.0+5.0)");
    }

    private void assertFormat(FractionEncapsulator frac, List<Monomial> list, String r) {
        assertThat(String.join("", frac.getEncapsulationString(list, 0, null))).isEqualTo(r);
    }
}
