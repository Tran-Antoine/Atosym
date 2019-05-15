package net.akami.mask.expression;

import net.akami.mask.operation.MaskContext;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VariableTest {

    @Test
    public void combineVars() {
        assertCombineVars(new char[]{'y'}, new char[]{'y'}, "y^2.0");
        assertCombineVars(new char[]{'x'}, new char[]{'y'}, "xy");
        assertCombineVars(new char[]{'x', 'y'}, new char[]{'x', 'y'}, "x^2.0y^2.0");
    }

    @Test
    public void dissociateVars() {
        Variable[] simple = {new Variable('x', Expression.of(5), MaskContext.DEFAULT)};
        Variable[] fraction = {new Variable('x', Expression.of(2.5f), MaskContext.DEFAULT)};
        Variable[] irreducible = {new Variable('x', Expression.of('y'), MaskContext.DEFAULT)};
        assertDissociateVars(simple, "xxxxx");
        assertDissociateVars(fraction, "xxx^0.5");
        assertDissociateVars(irreducible, "x^y");
    }

    private void assertDissociateVars(Variable[] input, String result) {
        List<String> converted = Variable.dissociate(input)
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());
        assertThat(String.join("", converted)).isEqualTo(result);
    }

    private void assertCombineVars(char[] v1, char[] v2, String result) {
        Variable[] variables = Variable.combine(get(v1), get(v2));
        List<String> converted = Arrays.asList(variables)
                .stream()
                .map(Variable::getExpression)
                .collect(Collectors.toList());

        assertThat(String.join("", converted)).isEqualTo(result);
    }

    private Variable[] get(char... input) {
        Variable[] vars = new Variable[input.length];

        int i = 0;
        for(char s : input) {
            vars[i++] = new Variable(s, null, null);
        }
        return vars;
    }
}
