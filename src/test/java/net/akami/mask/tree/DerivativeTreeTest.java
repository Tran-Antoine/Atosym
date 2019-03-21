package net.akami.mask.tree;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DerivativeTreeTest {

    @Test
    public void simpleDerivatives() {
        assertDerivative("2x", "2");
        assertDerivative("x", "1");
    }

    @Test
    public void polynomialDerivatives() {
        assertDerivative("1+2", "0");
        assertDerivative("1+x", "1");
        assertDerivative("x^2+2x+1", "2x+2");
        assertDerivative("3x^3 + 6x^2 + 3x", "9x^2+12x+3");
    }

    // TODO : fix the problem that 2*3 does not become 6 because they are not branches
    @Test
    public void powDerivatives() {
        assertDerivative("(3x+3)^2", "2*(3x+3)*3");
        assertDerivative("(x+3)^2", "2*(x+3)");
    }

    public void assertDerivative(String a, String b) {
        DerivativeTree tree = new DerivativeTree(a);
        Assertions.assertThat(tree.merge()).isEqualTo(b);
    }
}
