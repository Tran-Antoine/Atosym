package net.akami.mask.function;

import net.akami.mask.utils.ReducerFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class PiValueTest {

    @Test
    public void piTest() {
        Assertions.assertThat(ReducerFactory.reduce("cos(pi())").toString()).isEqualTo("-1.0");
        Assertions.assertThat(ReducerFactory.reduce("sqrt(sqrt(16))").toString()).isEqualTo("2.0");
    }
}
