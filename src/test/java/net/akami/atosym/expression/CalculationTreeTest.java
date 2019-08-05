package net.akami.atosym.expression;

import net.akami.atosym.tree.CalculationTree;
import net.akami.atosym.tree.ReducerTree;
import org.junit.Test;

import static net.akami.atosym.core.MaskContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class CalculationTreeTest {

    @Test
    public void simpleTree() {
        assertCalculationTree("5+3", "8.0");
    }

    private void assertCalculationTree(String input, String expected) {
        CalculationTree tree = new ReducerTree(input, DEFAULT);
        assertThat(tree.merge().toString()).isEqualTo(expected);
    }
}
