package net.akami.mask.utils;

import net.akami.mask.utils.FormatterFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FormatterTest {

    @Test
    public void reducerFormatTest() {
        String s1 = "5cos(3)+4x";
        //Assertions.assertThat(FormatterFactory.deleteMathShortcuts(s1)).isEqualTo("5*#*(3)+4*x");
    }

    @Test
    public void trigonometryTest() {
        String s1 = "cos(25)+cos(20)";
        //FormatterFactory.deleteMathShortcuts(s1);
    }
}
