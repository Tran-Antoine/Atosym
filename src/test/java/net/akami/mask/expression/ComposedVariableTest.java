package net.akami.mask.expression;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import net.akami.mask.function.TangentFunction;
import net.akami.mask.handler.Multiplier;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import static net.akami.mask.core.MaskContext.DEFAULT;

public class ComposedVariableTest {

    private final Multiplier multiplier = new Multiplier(DEFAULT);

    @Test
    public void getExpressionTest() {

        List<ExpressionElement> elements = Arrays.asList(
                new NumberElement(3), new ExpressionElement(3, new SimpleVariable('x', DEFAULT)));

        List<ExpressionEncapsulator> layers = Arrays.asList(
                new CosineFunction(DEFAULT),
                new SinusFunction(DEFAULT),
                new TangentFunction(DEFAULT)
        );

        List<ExpressionEncapsulator> layers2 = Arrays.asList(
                new CosineFunction(DEFAULT),
                new TangentFunction(DEFAULT),
                new SinusFunction(DEFAULT)
        );

        IrreducibleVarPart cVar1 = new IrreducibleVarPart(elements, layers);
        IrreducibleVarPart cVar2 = new IrreducibleVarPart(elements, layers);
        IrreducibleVarPart cVar3 = new IrreducibleVarPart(elements, layers2);
        assertThat(cVar1).isEqualTo(cVar2);
        assertThat(cVar1).isNotEqualTo(cVar3);
    }

    @Test
    public void composedMultTest() {

        List<ExpressionEncapsulator> layers = Arrays.asList(new CosineFunction(DEFAULT), new SinusFunction(DEFAULT));
        Expression insights = multiplier.operate(Expression.of('x'), Expression.of('y'));
        ExpressionElement m1 = new ExpressionElement(2, new IrreducibleVarPart(insights.getElements(), layers));
        ExpressionElement m2 = new ExpressionElement(5, new IrreducibleVarPart(insights.getElements(), layers));

        Expression result = multiplier.operate(Expression.of(m1), Expression.of(m2));
        assertThat(result.toString()).isEqualTo("10.0sin(cos(xy))^2.0");
    }
}
