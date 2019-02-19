package net.akami.mask.utils;

import net.akami.mask.math.OperationSign;
import net.akami.mask.math.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.akami.mask.math.Tree.Branch;

public class TreeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeUtils.class.getName());

    public static String mergeBranches(Tree tree) {
        /*
            If we give a very simple expression such as '50' to the reducer, it will detect that no operation
            needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if(tree.getBranches().size() == 1) {
            return tree.getBranches().get(0).getExpression();
        }

        /*
            Merging the branches from the last one to the first one (this initial expression)
         */
        for (int i = tree.getBranches().size() -1; i >= 0; i--) {

            Branch branch = tree.getBranches().get(i);
            LOGGER.debug("\n");
            LOGGER.debug("Actual branch : {}", branch.getExpression());

            if(!branch.canBeCalculated()) {
                LOGGER.debug("Not calculable : hasChildren : {} / children have no children : {}",
                        branch.hasChildren(), branch.doChildrenHaveChildren());
                continue;
            }
            // We are sure that the branch has a left and a right part, because the branch can be calculated
            String left = branch.getLeft().getExpression();
            String right = branch.getRight().getExpression();

            //  We need to check whether a reduced form has been calculated or not
            if(branch.getLeft().isReduced()) {
                left = branch.getLeft().getReducedValue();
            }
            if(branch.getRight().isReduced()) {
                right = branch.getRight().getReducedValue();
            }
            LOGGER.debug("Left : {}, Right : {}. Operation : {}", left, right, branch.getOperation());
            String value = OperationSign.getBySign(branch.getOperation()).compute(left, right);
            // The result is defined as the reduced value of the expression
            LOGGER.debug("Successfully calculated the value of "+branch.getExpression()+" : "+value);

            branch.setReducedValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if(first.isReduced()) {
                if(String.valueOf(first.getReducedValue()).equals("Infinity"))
                    throw new ArithmeticException();

                return String.valueOf(first.getReducedValue());
            }
        }
        return null;
    }

    public static void createNewBranch(Tree tree, Branch actual, int index, char operation) {

        String exp = actual.getExpression();

        int start = 0;

        if(exp.charAt(0) == '(' && exp.charAt(exp.length()-1) == ')') {
            start = 1;
        }

        String left = exp.substring(start, index);
        String right = exp.substring(index+1, exp.length()-start);
        actual.setLeft(tree.new Branch(left));
        actual.setRight(tree.new Branch(right));
        actual.setOperation(operation);

        LOGGER.debug("Successfully created new branches. Left : {}, Right : {}",
                actual.getLeft().getExpression(), actual.getRight().getExpression());
    }

    public static void printBranches(Tree self) {
        for(Branch branch : self.getBranches()) {
            LOGGER.debug("Branch found : {}", branch);
        }
    }
}
