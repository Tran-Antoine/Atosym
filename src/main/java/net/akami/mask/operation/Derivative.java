package net.akami.mask.operation;

import net.akami.mask.tree.BinaryTree;
import net.akami.mask.tree.Branch;
import net.akami.mask.operation.sign.QuaternaryOperationSign;
import net.akami.mask.tree.Reducer;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Does not work yet
 */
public class Derivative {

    private static final Derivative INSTANCE = new Derivative();
    private static final QuaternaryOperationSign[] PROCEDURAL_OPERATIONS;
    private static final Logger LOGGER = LoggerFactory.getLogger(Derivative.class);

    static {
        PROCEDURAL_OPERATIONS = new QuaternaryOperationSign[]{
                QuaternaryOperationSign.DIFF_SUM,
                QuaternaryOperationSign.DIFF_SUBTRACT,
                QuaternaryOperationSign.DIFF_MULT,
                QuaternaryOperationSign.DIFF_DIVIDE,
                QuaternaryOperationSign.DIFF_POW,
                QuaternaryOperationSign.NONE
        };
    }

    public String differentiate(String origin) {

        String formatted = FormatterFactory.formatForCalculations(origin);
        BinaryTree<Branch> tree = new Reducer(formatted);
        return null;
    }

    public void mergeBranches(BinaryTree<Branch> tree) {

        for(int i = tree.getBranches().size() - 1; i >= 0; i--) {
            Branch branch = tree.getBranches().get(i);
            LOGGER.info("Actual branch : {}", branch.getExpression());

            if (!branch.canBeEvaluated()) {
                LOGGER.info("Not calculable : hasChildren : {} / children have no children : {}",
                        branch.hasChildren(), branch.doChildrenHaveChildren());
                continue;
            }
            // We are sure that the branch has a left and a right part, because the branch can be calculated
            String left = branch.getLeft().getExpression();
            String right = branch.getRight().getExpression();

            //  We need to check whether a reduced form has been calculated or not
            if (branch.getLeft().hasReducedValue()) {
                left = branch.getLeft().getReducedValue();
            }
            if (branch.getRight().hasReducedValue()) {
                right = branch.getRight().getReducedValue();
            }
            LOGGER.debug("Left : {}, Right : {}, Operation : {}", left, right, branch.getOperation());
            String value = QuaternaryOperationSign.getBySign(branch.getOperation()).compute(left, null, null, right);
            // The result is defined as the reduced value of the expression
            LOGGER.info("Successfully calculated the value of " + branch.getExpression() + " : " + value);

            branch.setReducedValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if (first.hasReducedValue()) {
                if (String.valueOf(first.getReducedValue()).equals("Infinity"))
                    throw new ArithmeticException();

            }
        }
    }

    public static String differentiateElement(String element) {
        if(ExpressionUtils.isANumber(element))
            return "0";

        if(element.length() == 1)
            return "1";

        return MathUtils.diffPow(String.valueOf(element.charAt(0)), null, element.substring(2), null);
    }

    public static Derivative getInstance() {
        return INSTANCE;
    }
}
