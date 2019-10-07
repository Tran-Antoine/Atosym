package net.akami.atosym.expression;

import net.akami.atosym.function.MultOperator;

import static net.akami.atosym.core.MaskContext.DEFAULT;

public class VariableTest {

    private final MultOperator multiplier = new MultOperator(DEFAULT);

    /*@Test
    public void combineVars() {
        assertCombineVars(new char[]{'y'}, new char[]{'y'}, "y^2.0");
        assertCombineVars(new char[]{'x'}, new char[]{'y'}, "xy");
        assertCombineVars(new char[]{'x', 'y'}, new char[]{'x', 'y'}, "x^2.0y^2.0");
    }

    @Test
    public void dissociateVars() {
        ExponentOverlay exp1 = ExponentOverlay.fromExpression(Expression.of(5));
        ExponentOverlay exp2 = ExponentOverlay.fromExpression(Expression.of(2.5f));
        ExponentOverlay exp3 = ExponentOverlay.fromExpression(Expression.of('y'));
        Monomial simpleX = new Monomial('x', DEFAULT);

        IntricateVariable[] simple = {new IntricateVariable(simpleX, exp1)};
        IntricateVariable[] fraction = {new IntricateVariable(simpleX, exp2)};
        IntricateVariable[] irreducible = {new IntricateVariable(simpleX, exp3)};
        assertDissociateVars(Arrays.asList(simple), "xxxxx");
        assertDissociateVars(Arrays.asList(fraction), "xxx^0.5");
        assertDissociateVars(Arrays.asList(irreducible), "x^y");
    }

    @Test
    public void sortingVars() {
        SingleCharVariable simple = new SingleCharVariable('x', DEFAULT);
        ExponentOverlay squared = ExponentOverlay.fromExpression(Expression.of(2));
        IntricateVariable complex = new IntricateVariable(new Monomial('x',DEFAULT), squared);
        List<Variable> list = Arrays.asList(simple, complex);
        list.sort(VariableComparator.COMPARATOR);
        System.out.println(list);
    }

    private void assertDissociateVars(List<Variable> input, String result) {
        /*List<String> converted = VariableUtils.decomposeVariables(input)
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());
        assertThat(String.join("", converted)).isEqualTo(result);
    }

    private void assertCombineVars(char[] v1, char[] v2, String result) {
        SequencedMerge<Variable> combination = new VariableCombination(DEFAULT);
        List<Variable> variables = combination.merge(get(v1), get(v2),  false);
        List<String> converted = variables
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());

        assertThat(String.join("", converted)).isEqualTo(result);
    }

    /*
    @Test
    public void getComplexExpressionTest() {

        Monomial m = new Monomial('x', DEFAULT);
        IntricateVariable c1 = new IntricateVariable(m);
        assertThat(c1.getExpression()).isEqualTo("x");
    }

    @Test
    public void recursiveComplexTest() {
        Expression result = FastAtosymMath.reduce("y^4.0+5.0x");
        IntricateVariable complex = new IntricateVariable(new Monomial(1, new IntricateVariable(result.getElements())));
        Monomial m1 = new NumberElement(4);
        Monomial m2 = new Monomial(1, complex);
        assertThat(multiplier.simpleMult(m1, m2)).isEqualTo("4.0y^4.0+20.0x");
    }

    private List<Variable> get(char... input) {
        SingleCharVariable[] vars = new SingleCharVariable[input.length];

        int i = 0;
        for(char s : input) {
            vars[i++] = new SingleCharVariable(s, DEFAULT);
        }
        return Arrays.asList(vars);
    }*/
}
