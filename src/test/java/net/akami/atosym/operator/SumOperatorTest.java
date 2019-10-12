package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.utils.ParserUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.akami.atosym.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class SumOperatorTest {

    private final SumOperator adder = DEFAULT.getBinaryOperator(SumOperator.class);

    @Test
    public void numeric_sum() {
        assertSum("5", "2", "7.0");
        assertSum("0.5", "2.0", "1.5");
    }

    @Test
    public void sum_involving_monomials_with_different_literal_parts() {
        assertSum("2", "x", "2.0+x");
        assertSum("a", "b", "a+b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void numeric_sum_involving_more_than_two_numbers() {
        // Binary operators cannot manage other than two elements
        adder.rawOperate(Stream.of("5", "2", "3").map(this::toMathObject).collect(Collectors.toList()));
    }

    @Test
    public void sum_involving_more_than_two_elements() {
        assertSum("2", "x+2", "4.0+x");
        assertSum("2+x", "2+x", "4.0+2.0x");
        assertSum("x+2+y", "x+2", "2.0x+4.0+y");
    }

    @Test
    public void sum_of_simple_mult() {
        assertSum("x", "5x", "6.0x");
        assertSum("5x", "x", "6.0x");
        assertSum("5x", "3x", "8.0x");
    }

    @Test
    public void sum_of_incompatible_simple_mult() {
        assertSum("5xy", "3x", "5.0xy+3.0x");
    }

    @Test
    public void sum_of_mult_with_squared_variable() {
        assertSum("5xx", "4xx", "9.0x^2.0");
    }

    @Test
    public void manually_built_sum_between_two_elements() {
        MaskContext context = new MaskContext.Builder()
                .withOperators(SumOperator::new)
                .build();

        assertSimpleSum(new NumberExpression(3f), new NumberExpression(5f), context, "8.0");
        assertSimpleSum(new NumberExpression(4f), new VariableExpression('x'), context, "4.0+x");
        assertSimpleSum(new VariableExpression('x'), new VariableExpression('x'), context, "2.0x");
    }

    private void assertSimpleSum(MathObject a, MathObject b, MaskContext context, String result) {
        List<MathObject> elements = toList(a, b);
        SumOperator operator = context.getBinaryOperator(SumOperator.class);
        assertThat(operator.rawOperate(elements).testDisplay()).isEqualTo(result);
    }

    private List<MathObject> toList(MathObject... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }

    private void assertSum(String a, String b, String result) {
        List<MathObject> objects = new ArrayList<>();
        objects.add(toMathObject(a));
        objects.add(toMathObject(b));
        assertThat(adder.rawOperate(objects).testDisplay()).isEqualTo(result);
    }

    private MathObject toMathObject(String input) {
        return ParserUtils.generateSimpleTree(input).merge();
    }

     /*@Test
    public void sumTest() {
        assertSimpleSum("2.0x+5.0", "5", "2x");
        assertSimpleSum("8.0x", "5x", "3x");
        assertSimpleSum("x", "x/2", "x/2");
        assertSimpleSum("1.0", "1/2", "1/2");
        assertSimpleSum("x", "-1", "x+1");
        assertSimpleSum("4.0x-2.0", "x", "3x-2");
        assertSimpleSum("y^2.0+y", "0", "(y^2+y)");
        assertSimpleSum("y^2.0+y", "y", "y^2");
        //assertSimpleSum("2xyz+2xyz+2xyz", "","6.0xyz");
        assertSimpleSum("5.0x^2.0y+1.0", "5x^2y", "1");
        assertSimpleSum("y+0.4", "y", "0.4");
        assertSimpleSum(("y+0.5"), "y", "1/2");
        assertSimpleSum("y+0.4", "y", "2/5");
    }*/
}
