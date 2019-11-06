package net.akami.atosym.expression;

import net.akami.atosym.tree.AbstractSyntaxTree;
import net.akami.atosym.utils.ParserFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculationTreeTest {

    @Test
    public void simpleTree() {
        assertCalculationTree("5+3", "8.0");
    }

    private void assertCalculationTree(String input, String expected) {
        AbstractSyntaxTree tree = ParserFactory.generateSimpleTree(input);
        assertThat(tree.merge().testDisplay()).isEqualTo(expected);
    }
}
