package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.utils.ReducerFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AdderTest {

    private final Adder adder = MaskContext.DEFAULT.getBinaryOperation(Adder.class);

    @Test
    public void numericTest() {
        assertSum("2", "5","7.0");
    }

    @Test
    public void sumTest() {
        assertSum("2x", "5","2.0x+5.0");
        assertSum("3x", "5x","8.0x");
        //assertSum("x/2", "x/2","1.0x");
        //assertSum("1/2", "1/2","1.0");
        assertSum("x+1", "-1","x");
        assertSum("3x-2", "x","4.0x-2.0");
        //assertSum("((x)#)^2", "","((x)#)^2.0");
        assertSum("(y^2+y)", "0","y^2.0+y");
        assertSum("y^2", "y","y^2.0+y");
        //assertSum("2xyz+2xyz+2xyz", "","6.0xyz");
        assertSum("1", "5x^2y","5.0x^2.0y+1.0");
        assertSum("0.4", "y","y+0.4");
        //assertSum("1/2", "y",("0.5+y"));
        //assertSum("2/5", "y","0.4+y");
    }

    @Test
    public void monomialSumTest() {
        List<String> monomials = Arrays.asList("2xyz", "2xyz", "2xyz");
        //assertThat(adder.monomialSum(monomials, true)).isEqualTo("6xyz");
    }


    private void assertSum(String a, String b, String result) {
        Expression aExp = ReducerFactory.reduce(a);
        Expression bExp = ReducerFactory.reduce(b);
        assertThat(adder.operate(aExp, bExp).toString()).isEqualTo(result);
    }
}
