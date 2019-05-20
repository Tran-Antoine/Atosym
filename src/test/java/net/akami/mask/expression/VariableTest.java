package net.akami.mask.expression;

import net.akami.mask.core.MaskContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableTest {

    @Test
    public void combineVars() {
        assertCombineVars(new char[]{'y'}, new char[]{'y'}, "y^2.0");
        assertCombineVars(new char[]{'x'}, new char[]{'y'}, "xy");
        assertCombineVars(new char[]{'x', 'y'}, new char[]{'x', 'y'}, "x^2.0y^2.0");
    }

    @Test
    public void dissociateVars() {
        SimpleVariable[] simple = {new SimpleVariable('x', Expression.of(5), MaskContext.DEFAULT)};
        SimpleVariable[] fraction = {new SimpleVariable('x', Expression.of(2.5f), MaskContext.DEFAULT)};
        SimpleVariable[] irreducible = {new SimpleVariable('x', Expression.of('y'), MaskContext.DEFAULT)};
        assertDissociateVars(Arrays.asList(simple), "xxxxx");
        assertDissociateVars(Arrays.asList(fraction), "xxx^0.5");
        assertDissociateVars(Arrays.asList(irreducible), "x^y");
    }

    private void assertDissociateVars(List<Variable> input, String result) {
        List<String> converted = Variable.dissociate(input)
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());
        assertThat(String.join("", converted)).isEqualTo(result);
    }

    private void assertCombineVars(char[] v1, char[] v2, String result) {
        List<Variable> variables = Variable.combine(get(v1), get(v2), null);
        List<String> converted = variables
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());

        assertThat(String.join("", converted)).isEqualTo(result);
    }

    private List<Variable> get(char... input) {
        SimpleVariable[] vars = new SimpleVariable[input.length];

        int i = 0;
        for(char s : input) {
            vars[i++] = new SimpleVariable(s, null, null);
        }
        return Arrays.asList(vars);
    }
}
