package net.akami.mask.operation;

import net.akami.mask.math.BinaryTree;
import net.akami.mask.math.BinaryTree.Branch;
import net.akami.mask.operation.sign.QuaternaryOperationSign;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        BinaryTree tree = new BinaryTree();
        tree.new Branch(formatted);
        return null;
    }

    public void mergeBranches(BinaryTree tree) {

        for(int i = tree.getBranches().size() - 1; i >= 0; i--) {
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
            String value = QuaternaryOperationSign.getBySign(branch.getOperation()).compute(left, null, null, right);
            // The result is defined as the reduced value of the expression
            LOGGER.info("Successfully calculated the value of " + branch.getExpression() + " : " + value);

            branch.setAlternativeValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if (first.hasAlternativeValue()) {
                if (String.valueOf(first.getAlternativeValue()).equals("Infinity"))
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
