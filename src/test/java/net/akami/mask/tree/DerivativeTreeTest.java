package net.akami.mask.tree;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DerivativeTreeTest {

    @Test
    public void simpleDerivatives() {
        assertDerivative("2x", "2", 'x');
        assertDerivative("x", "1", 'x');
    }

    @Test
    public void polynomialDerivatives() {
        assertDerivative("1+2", "0", 'x');
        assertDerivative("1+x", "1", 'x');
        assertDerivative("x^2+2x+1", "2x+2", 'x');
        assertDerivative("3x^3 + 6x^2 + 3x", "9x^2+12x+3", 'x');
    }

    // TODO : fix the problem that 2*3 does not become 6 because they are not branches
    @Test
    public void powDerivatives() {
        assertDerivative("(3x+3)^2", "2*(3x+3)*3", 'x');
        assertDerivative("(x+3)^2", "2*(x+3)", 'x');
    }

    @Test
    public void divisionDerivatives() {
        assertDerivative("(5x^2+3x)/(4x+1)", "(20x^2+10x+3)/(4x+1)^2", 'x');
    }

    @Test
    public void multiVariablesDerivatives() {
        assertDerivative("5x", "0", 'y');
        assertDerivative("5x+y", "1", 'y');
        assertDerivative("5xy", "5x", 'y');
    }
    public void assertDerivative(String a, String b, char var) {
        DerivativeTree tree = new DerivativeTree(a, var);
        Assertions.assertThat(tree.merge()).isEqualTo(b);
    }
}
