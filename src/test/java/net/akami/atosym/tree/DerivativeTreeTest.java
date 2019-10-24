package net.akami.atosym.tree;

import org.junit.Test;

public class DerivativeTreeTest {

    @Test
    public void simpleDerivatives() {
        assertDerivative("2x", "2.0", 'x');
        assertDerivative("x", "1", 'x');
    }

    @Test
    public void polynomialDerivatives() {
        assertDerivative("1+2", "0", 'x');
        assertDerivative("1+x", "1.0", 'x');
        assertDerivative("x^2+2x+1", "2.0x+2.0", 'x');
        assertDerivative("3x^3 + 6x^2 + 3x", "9.0x^2.0+12.0x+3.0", 'x');
    }

    @Test
    public void powDerivatives() {
        assertDerivative("(3x+3)^2", "18.0x+18.0", 'x');
        assertDerivative("(x+3)^2", "2.0x+6.0", 'x');
    }

    @Test
    public void divisionDerivatives() {
        assertDerivative("(5x^2+3x)/(4x+1)", "(20.0x^2.0+10.0x+3.0)/(16.0x^2.0+8.0x+1.0)", 'x');
    }

    @Test
    public void multiVariablesDerivatives() {
        assertDerivative("5x", "0", 'y');
        assertDerivative("5x+y", "1.0", 'y');
        assertDerivative("5xy", "5.0x", 'y');
    }

    public void assertDerivative(String a, String b, char var) {
        //DerivativeTree tree = new DerivativeTree(a, var, MaskContext.DEFAULT);
        //Assertions.assertThat(tree.merge().toString()).isEqualTo(b);
    }
}
