package net.akami.mask.tree;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FormatterTreeTest {

    @Test
    public void simpleMult() {
        assertTree("5*x", "5x");
    }

    @Test
    public void trickyMult() {
        assertTree("5*x*y*z", "5xyz");
    }

    @Test
    public void simpleTrigonometry() {
        assertTree("5*((x)@)", "5sin(x)");
        assertTree("((x)@)", "sin(x)");
    }

    @Test
    public void trickyTrigonometry() {
        assertTree("((((x)@))@)", "sin(sin(x))");
        assertTree("((((x)@))#)", "cos(sin(x))");
        assertTree("5((((x)@))#)", "5cos(sin(x))");
    }

    @Test
    public void complexExpression() {
        assertTree("1+x", "1+x");
        assertTree("1+5*x", "1+5x");
        assertTree("5*((25)@)+1", "5sin(25)+1");
        assertTree("5*(2+x+y)", "5(2+x+y)");
    }

    @Test
    public void bracketsExpression() {
        assertTree("(5+x)^2", "(5+x)^2");
    }

    public void assertTree(String a, String b) {
        FormatterTree t = new FormatterTree(a);
        Assertions.assertThat(t.merge()).isEqualTo(b);
    }
}
