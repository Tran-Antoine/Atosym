package net.akami.mask.tree;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FormatterTreeTest {

    @Test
    public void simpleMult() {
        FormatterTree t = new FormatterTree("5*x");
        System.out.println(t.getBranches());
        Assertions.assertThat(t.merge()).isEqualTo("5x");
    }

    @Test
    public void trickyMult() {
        FormatterTree t = new FormatterTree("5*x*y*z");

        Assertions.assertThat(t.merge()).isEqualTo("5xyz");
    }

    @Test
    public void simpleTrigonometry() {
        FormatterTree t2 = new FormatterTree("5*((x)@)");
        Assertions.assertThat(t2.merge()).isEqualTo("5sin(x)");

        FormatterTree t = new FormatterTree("((x)@)");
        Assertions.assertThat(t.merge()).isEqualTo("sin(x)");
    }

    @Test
    public void trickyTrigonometry() {
        FormatterTree t = new FormatterTree("((((x)@))@)");
        FormatterTree t2 = new FormatterTree("((((x)@))#)");
        FormatterTree t3 = new FormatterTree("5((((x)@))#)");

        Assertions.assertThat(t.merge()).isEqualTo("sin(sin(x))");
        Assertions.assertThat(t2.merge()).isEqualTo("cos(sin(x))");
        Assertions.assertThat(t3.merge()).isEqualTo("5cos(sin(x))");
    }

    @Test
    public void complexExpression() {
        assertTree("1+x", "1+x");
        assertTree("1+5*x", "1+5x");
        assertTree("5*((25)@)+1", "5sin(25)+1");
        assertTree("5*(2+x+y)", "5(2+x+y)");
    }

    public void assertTree(String a, String b) {
        FormatterTree t = new FormatterTree(a);
        Assertions.assertThat(t.merge()).isEqualTo(b);
    }
}
