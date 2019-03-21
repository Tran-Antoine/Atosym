package net.akami.mask.tree;

import net.akami.mask.operation.sign.BinaryOperationSign;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.TreeUtils;

public class CalculationTree extends BinaryTree {

    public CalculationTree(String initial) {
        super(initial,'+', '-', '*', '/', '^', ' ');
    }

    @Override
    protected void create(Branch self) {
        for (int i = 0; i < getSplitters().length; i += 2) {
            split(self, getSplitters()[i], getSplitters()[i + 1]);
        }
    }

    @Override
    protected void split(Branch self, char... by) {
        /*
          Avoids splits with the roots parts. For instance, when splitting by '+' and '-' for the
          expression '5+3*2', the result will be 5+3*2, 5, and 3*2. When splitting by '*' and '/', we don't
          want '5+3*2' to be split, because it contains one of the previous operations (+).
        */
        if (self.hasChildren())
            return;

        char c1 = by[0];
        char c2 = by[1];
        String expression = self.getExpression();
        /*
            We must go from the end to the beginning. Otherwise, operations' priority is not respected.
            For instance, 2/2*2 = 1. If we go from 0 to exp.length() -1, the expression will be divided like this :
            2 |/| 2*2. 2*2 will be calculated first, the final result will be 1/2. If we go from exp.length() -1
            to 0, the expression will be divided like this :
            2 / 2 |*| 2. 2/2 will be calculated first, the final result will be 2.
        */
        for (int i = expression.length() - 1; i >= 0; i--) {


            char c = expression.charAt(i);

            if ((c == c1 || c == c2)) {
                LOGGER.debug("Checking if sign {} at index {} is surrounded in {}", c, i, this);
                boolean bracketsConnected = ExpressionUtils.areEdgesBracketsConnected(expression, false);

                if (!ExpressionUtils.isSurroundedByParentheses(i, expression)) {
                    LOGGER.debug("Found a place to split at index {}, character '{}'", i, c);
                    TreeUtils.createNewBranch(this, self, i, c, bracketsConnected);
                    break;
                }
            }
        }
        LOGGER.debug("Separations with the operations {} and {} are now done", c1, c2);
    }

    @Override
    protected void evalBranch(Branch self) {
        LOGGER.info("Actual branch : {}", self.getExpression());

        // We are sure that the branch has a left and a right part, because the branch can be calculated
        String left = self.getLeftValue();
        String right = self.getRightValue();

        LOGGER.debug("Left : {}, Right : {}, Operation : {}", left, right, self.getOperation());
        String value = BinaryOperationSign.getBySign(self.getOperation()).compute(left, right);
        // The result is defined as the reduced value of the expression
        LOGGER.info("Successfully calculated the value of " + self.getExpression() + " : " + value);

        self.setAlternativeValue(value);
        self.setLeft(null);
        self.setRight(null);
    }
}
