package net.akami.mask.math;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.TreeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.akami.mask.utils.ReducerFactory.PROCEDURAL_OPERATIONS;
import java.util.ArrayList;
import java.util.List;

/**
 * A tree is an object that contains a list of branches.
 */
public class BinaryTree {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private List<Branch> branches;

    public BinaryTree() {
        this.branches = new ArrayList<>();
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public class Branch {

        private Branch left;
        private Branch right;
        private char operation;
        private String expression;
        private boolean reduced;
        private String alternativeValue;

        public Branch(String expression) {
            this.expression = deleteUselessBrackets(expression);
            reduced = false;
            branches.add(this);
            LOGGER.debug("Now treating : "+this);
            for (int i = 0; i < PROCEDURAL_OPERATIONS.length; i += 2) {
                split(PROCEDURAL_OPERATIONS[i].getSign(), PROCEDURAL_OPERATIONS[i + 1].getSign());
             }
        }

        private String deleteUselessBrackets(String exp) {
            while(ExpressionUtils.areEdgesBracketsConnected(exp)) {
                exp = exp.substring(1, exp.length()-1);
            }
            return exp;
        }

        private void split(char c1, char c2) {
            /*
                We must go from the end to the beginning. Otherwise, operations' priority is not respected.
                For instance, 2/2*2 = 1. If we go from 0 to exp.length() -1, the expression will be divided like this :
                2 |/| 2*2. 2*2 will be calculated first, the final result will be 1/2. If we go from exp.length() -1
                to 0, the expression will be divided like this :
                2 / 2 |*| 2. 2/2 will be calculated first, the final result will be 2.
             */
            for (int i = expression.length() - 1; i >= 0; i--) {
                /*
                    Avoids splits with the roots parts. For instance, when splitting by '+' and '-' for the
                    expression '5+3*2', the result will be 5+3*2, 5, and 3*2. When splitting by '*' and '/', we don't
                    want '5+3*2' to be split, because it contains one of the previous operations (+).
                 */
                if (hasChildren())
                    break;

                char c = expression.charAt(i);

                if ((c == c1 || c == c2)) {
                    LOGGER.debug("Checking if sign {} at index {} is surrounded in {}", c, i, this);
                    boolean bracketsConnected = ExpressionUtils.areEdgesBracketsConnected(expression);

                    if (!ExpressionUtils.isSurroundedByParentheses(i, expression)) {
                        LOGGER.debug("Found a place to split at index {}, character '{}'", i, c);
                        TreeUtils.createNewBranch(BinaryTree.this, this, i, c, bracketsConnected);
                        break;
                    }
                }
            }
            LOGGER.debug("Separations with the operations {} and {} are now done", c1, c2);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Branch) {
                return expression.equals(((Branch) obj).expression);
            }
            return false;
        }

        @Override
        public String toString() {
            return expression;
        }

        public boolean hasChildren() {
            return left != null || right != null;
        }

        public boolean doChildrenHaveChildren() {
            if (hasChildren())
                return left.hasChildren() || right.hasChildren();
            return false;
        }

        public boolean canBeCalculated() {
            if (!hasChildren()) {
                return false;
            }
            return !doChildrenHaveChildren();
        }

        public Branch getLeft()         { return left;         }
        public Branch getRight()        { return right;        }
        public char getOperation()      { return operation;    }
        public String getExpression()   { return expression;   }
        public String getAlternativeValue() { return alternativeValue; }
        public boolean hasAlternativeValue()      { return reduced;      }

        public void setOperation(char operation)  { this.operation = operation; }
        public void setLeft(Branch left)          { this.left = left;           }
        public void setRight(Branch right)        { this.right = right;         }
        public void setAlternativeValue(String value) { this.alternativeValue = value; reduced = true; }
    }
}
