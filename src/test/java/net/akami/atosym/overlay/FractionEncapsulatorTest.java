package net.akami.atosym.overlay;

import net.akami.atosym.expression.Monomial;
import net.akami.atosym.expression.NumberElement;
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

        FractionOverlay encapsulator = new FractionOverlay(5);
        FractionOverlay encapsulator2 = new FractionOverlay(list2);
        assertFormat(encapsulator, list, "/5.0");
        assertFormat(encapsulator, list2, "()/5.0");
        assertFormat(encapsulator2, list, "/(4.0+5.0)");
        assertFormat(encapsulator2, list2, "()/(4.0+5.0)");
    }

    private void assertFormat(FractionOverlay frac, List<Monomial> list, String r) {
        assertThat(String.join("", frac.getEncapsulationString(list, 0, null))).isEqualTo(r);
    }
}
