package net.akami.mask.utils;

import net.akami.mask.Operation;
import net.akami.mask.core.RestCalculation;
import net.akami.mask.core.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.akami.mask.core.Tree.Branch;

public class TreeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeUtils.class.getName());

    public static RestCalculation mergeBranches(Tree tree) {
        /*
            If we give a very simple expression such as '50' to the reducer, it will detect that no operation
            needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if(tree.getBranches().size() == 1) {
            return new RestCalculation(tree.getBranches().get(0).getExpression());
        }

        /*
            Merging the branches from the last one to the first one (this initial expression)
         */
        for (int i = tree.getBranches().size() -1; i >= 0; i--) {

            Branch branch = tree.getBranches().get(i);
            LOGGER.debug("Actual branch : {}\n", branch.getExpression());

            if(!branch.canBeCalculated()) {
                LOGGER.debug("Not calculable.");
                continue;
            }
            // We are sure that the branch has a left and a right part, because the branch can be calculated
            String left = branch.getLeft().getExpression();
            String right = branch.getRight().getExpression();

            /*  We need to check whether the left/right part is already reduced as a simple number, or whether
                a reduced form has been calculated
            */
            if(branch.getLeft().isReduced()) {
                left = String.valueOf(branch.getLeft().getReducedValue());
            }
            if(branch.getRight().isReduced()) {
                right = String.valueOf(branch.getRight().getReducedValue());
            }
            float value = Operation.getBySign(branch.getOperation()).compute(left, right);
            // The result is defined as the reduced value of the expression
            LOGGER.debug("Successfully calculated the value of "+branch.getExpression()+" : "+value);

            branch.setReducedValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if(first.isReduced()) {
                if(String.valueOf(first.getReducedValue()).equals("Infinity"))
                    throw new ArithmeticException();

                return new RestCalculation(String.valueOf(first.getReducedValue()));
            }
        }
        return null;
    }

    public static void createNewBranch(Tree tree, Branch actual, String exp, int index, char operation) {

        String left = exp.substring(0, index);
        String right = exp.substring(index+1);
        actual.setLeft(tree.new Branch(left));
        actual.setRight(tree.new Branch(right));
        actual.setOperation(operation);
    }
}
