package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.utils.ReducerFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiplierTest {

    private final Multiplier MULT = MaskContext.DEFAULT.getBinaryOperation(Multiplier.class);

    @Test
    public void multTest() {
        assertOperation("3", "x/3","x");
        assertOperation("x^11","x","x^12.0");
        assertOperation("x^11","x+2","x^12.0+2.0x^11.0");
        assertOperation("x^(y^2)","x^y","x^(y^2.0+y)");
        assertOperation("x^2+2xy+2xz+y^2+2yz+z^2", "x+y+z","x^3+3x^2y+3x^2z+3xy^2+6xyz+3xz^2+y^3+3y^2z+3yz^2+z^3");
        assertOperation("3","x","3.0x");
        assertOperation("3x","x","3.0x^2.0");
        assertOperation("x^2+2x+1","x+1","x^3.0+3.0x^2.0+3.0x+1.0");
        assertOperation("xy^2","x","x^2.0y^2.0");
        assertOperation("3x^2y+3xy^2+y^3","x+y","3x^3y+6x^2y^2+4xy^3+y^4");
        assertOperation("2xz+y^2", "x+y","2x^2z+2xyz+xy^2+y^3");
        assertOperation("2xz+y^2", "x+y+z","2x^2z+2xyz+2xz^2+xy^2+y^3+y^2z");
    }

    // IMPORTANT : A trigonometric expression must always be under the form ((something)@or#or§).
    // The edge brackets cannot be omitted, otherwise some unexpected results might occur.
    @Test
    public void trigoTest() {
        assertOperation("x", "@", "((x)@)");
        assertOperation("x+y", "#", "((x+y)#)");
        assertOperation("0", "@", "0.0");
        assertOperation("0", "#", "1.0");
        assertOperation("5", "((x)@)", "5((x)@)");
        assertOperation("((x)@)", "((x)@)", "((x)@)^2");
        assertOperation("((x)@)^2", "((x)@)", "((x)@)^3");
        assertOperation("((y)@)^2", "((x)@)^3", "((y)@)^2((x)@)^3");
    }

    @Test
    public void inFormatTest() {
        //assertThat(MULT.inFormat("8x+y-3")).isEqualTo("8x+y-3");
    }

    private void assertOperation(String i1, String i2, String result) {
        Expression e1 = ReducerFactory.reduce(i1);
        Expression e2 = ReducerFactory.reduce(i2);
        assertThat(MULT.operate(e1, e2).toString()).isEqualTo(result);
    }
}
