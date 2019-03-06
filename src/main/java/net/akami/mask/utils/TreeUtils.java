package net.akami.mask.utils;

import net.akami.mask.operation.sign.BinaryOperationSign;
import net.akami.mask.math.BinaryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.akami.mask.math.BinaryTree.Branch;

public class TreeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeUtils.class.getName());

    public static String mergeBranches(BinaryTree tree) {
        /*
            If we give a very simple expression such as '50' to the reducer, it will detect that no operation
            needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if (tree.getBranches().size() == 1) {
            LOGGER.debug("Only one branch found. Returns it.");
            return tree.getBranches().get(0).getExpression();
        }

        /*
            Merging the branches from the last one to the first one (this initial expression)
         */
        for (int i = tree.getBranches().size() - 1; i >= 0; i--) {

            Branch branch = tree.getBranches().get(i);
            LOGGER.info("Actual branch : {}", branch.getExpression());

            if (!branch.canBeCalculated()) {
                LOGGER.info("Not calculable : hasChildren : {} / children have no children : {}",
                        branch.hasChildren(), branch.doChildrenHaveChildren());
                continue;
            }
            // We are sure that the branch has a left and a right part, because the branch can be calculated
            String left = branch.getLeft().getExpression();
            String right = branch.getRight().getExpression();

            //  We need to check whether a reduced form has been calculated or not
            if (branch.getLeft().hasAlternativeValue()) {
                left = branch.getLeft().getAlternativeValue();
            }
            if (branch.getRight().hasAlternativeValue()) {
                right = branch.getRight().getAlternativeValue();
            }
            LOGGER.debug("Left : {}, Right : {}, Operation : {}", left, right, branch.getOperation());
            String value = BinaryOperationSign.getBySign(branch.getOperation()).compute(left, right);
            // The result is defined as the reduced value of the expression
            LOGGER.info("Successfully calculated the value of " + branch.getExpression() + " : " + value);

            branch.setAlternativeValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if (first.hasAlternativeValue()) {
                if (String.valueOf(first.getAlternativeValue()).equals("Infinity"))
                    throw new ArithmeticException();

                return String.valueOf(first.getAlternativeValue());// 8019
            }
        }
        return null;
    }

    public static void createNewBranch(BinaryTree tree, Branch actual, int index, char sign, boolean edgesBracketsConnected) {

        String exp = actual.getExpression();

        int start = edgesBracketsConnected ? 1 : 0;

        String left = exp.substring(start, index);
        // In case the expression is "-5", means "0-5"
        if(left.isEmpty())
            left = "0";
        String right = exp.substring(index + 1, exp.length() - start);
        LOGGER.debug("Successfully created new branches. Left : {}, Right : {}", left, right);
        actual.setLeft(tree.new Branch(left));
        actual.setRight(tree.new Branch(right));
        actual.setOperation(sign);
    }

    public static void printBranches(BinaryTree self) {
        for (Branch branch : self.getBranches()) {
            LOGGER.info("Branch found : {}", branch);
        }
    }
}
