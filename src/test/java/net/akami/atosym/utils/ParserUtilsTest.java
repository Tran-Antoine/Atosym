package net.akami.atosym.utils;

import net.akami.atosym.tree.AbstractSyntaxTree;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserUtilsTest {

    @Test
    public void simple_tree_merging_test() {
        AbstractSyntaxTree tree = ParserFactory.generateSimpleTree("2+2+3");
        assertThat(tree.merge().testDisplay()).isEqualTo("7.0");
    }
}
