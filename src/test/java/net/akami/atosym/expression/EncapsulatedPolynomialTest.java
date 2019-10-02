package net.akami.atosym.expression;

public class EncapsulatedPolynomialTest {

    /*@Test
    public void getExpressionTest() {

        List<Monomial> monomials = Arrays.asList(new Monomial(5, new SingleCharVariable('x', DEFAULT)),
                new NumberElement(3));

        List<ExpressionOverlay> layers = Arrays.asList(
                new ExponentOverlay(monomials),
                new CosineOperator(DEFAULT),
                new SineOperator(DEFAULT)
        );
        IntricateVariable polynomial = new IntricateVariable(monomials, layers);

        assertThat(polynomial.getExpression()).isEqualTo("sin(cos((5.0x+3.0)^(5.0x+3.0)))");
    }*/
}