package net.akami.atosym.function;

import net.akami.atosym.utils.FastAtosymMath;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PiValueTest {

    @Test
    public void piTest() {
        Assertions.assertThat(FastAtosymMath.reduce("cos(pi())").toString()).isEqualTo("-1.0");
        Assertions.assertThat(FastAtosymMath.reduce("sqrt(sqrt(16))").toString()).isEqualTo("2.0");
    }
}
