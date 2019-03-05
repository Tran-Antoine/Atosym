package net.akami.mask;

import net.akami.mask.operation.Division;
import net.akami.mask.utils.MathUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void sumTest() {

        Assertions.assertThat(MathUtils.sum("0.4", "y")).isEqualTo("0.4+y");
        Assertions.assertThat(MathUtils.sum("1/2", "y")).isEqualTo("0.5+y");
        Assertions.assertThat(MathUtils.sum("2/5", "y")).isEqualTo("0.4+y");
        //Assertions.assertThat(MathUtils.monomialSum("2y/3", "5/3")).isEqualTo("2y/3+5/3");
    }

    @Test
    public void divideTest() {
        //Assertions.assertThat(MathUtils.divide("6.4+6.4z", "3.2")).isEqualTo("2+2*z");
        Assertions.assertThat(Division.getInstance().simpleDivision("-2x", "4")).isEqualTo("x/-2");
        //Assertions.assertThat(MathUtils.divide("6.2+0.2z", "1.8")).isEqualTo("3.444444+0.11111111z");
    }
}
