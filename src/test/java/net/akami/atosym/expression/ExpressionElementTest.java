package net.akami.atosym.expression;

public class ExpressionElementTest {

    /*@Test
    public void compareDifferentLayers() {

        List<ExpressionOverlay> layers1 = Arrays.asList(
                new CosineOperator(DEFAULT),
                new TangentOperator(DEFAULT)
        );
        List<ExpressionOverlay> layers2 = Arrays.asList(
                new CosineOperator(DEFAULT),
                new SineOperator(DEFAULT)
        );

        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        IntricateVariable c1 = new IntricateVariable(single,layers1);
        IntricateVariable c2 = new IntricateVariable(single, layers2);

        assertThat(c1.equals(c2)).isEqualTo(false);
    }

    @Test
    public void compareSameLayersInDifferentOrders() {

        List<ExpressionOverlay> layers1 = Arrays.asList(
                new CosineOperator(DEFAULT),
                new SineOperator(DEFAULT)
        );
        List<ExpressionOverlay> layers2 = Arrays.asList(
                new SineOperator(DEFAULT),
                new CosineOperator(DEFAULT)
        );
        List<Monomial> single = Collections.singletonList(new NumberElement(1));

        IntricateVariable m1 = new IntricateVariable(single, layers1);
        IntricateVariable m2 = new IntricateVariable(single, layers2);

        SequencedMerge<Variable> behavior = new VariableCombination(DEFAULT);
        assertThat(m1.equals(m2)).isEqualTo(false);
        assertThat(behavior.generateElementProperties(m1, m2).get(0).prepare()).isEqualTo(false);
    }*/
}
