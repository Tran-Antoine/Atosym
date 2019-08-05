package net.akami.atosym.utils;

import net.akami.atosym.tree.BinaryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.akami.atosym.tree.Branch;

public class TreeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeUtils.class.getName());

    public static <T extends Branch<T>> void createNewBranch(BinaryTree<T> tree, T actual, int index, char sign, boolean edgesBracketsConnected) {

        String exp = actual.getExpression();

        int start = edgesBracketsConnected ? 1 : 0;

        String left = exp.substring(start, index);
        // In case the expression is "-5", means "0-5"
        if(left.isEmpty())
            left = "0";
        String right = exp.substring(index + 1, exp.length() - start);
        LOGGER.debug("Successfully created new branches. Left : {}, Right : {}", left, right);
        actual.setLeft(tree.load(left));
        actual.setRight(tree.load(right));
        actual.setOperation(sign);
    }

    public static void printBranches(BinaryTree<? extends Branch> self) {
        for (Branch branch : self) {
            LOGGER.info("Branch found : {}", branch);
        }
    }
}
