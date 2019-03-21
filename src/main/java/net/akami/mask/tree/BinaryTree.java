package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tree is an object that contains a list of branches.
 */
public abstract class BinaryTree implements Iterable<BinaryTree.Branch> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private List<Branch> branches;
    private char[] splitters;

    public BinaryTree(String initial, char... splitters) {
        this.branches = new ArrayList<>();
        this.splitters = splitters;
        new Branch(initial);
    }

    /**
     * Defines how each branch must be split. Note that checks concerning size / values of the char array are
     * usually not needed, since the user is supposed to know what the array must be.
     * @param self the current branch not split yet
     * @param by the 'splitters', which are used to determine the left and right part of the branch
     */
    protected abstract void split(Branch self, char... by);
    protected abstract void create(Branch self);

    /**
     * @param self a calculable-guaranteed branch. Note that if may not be guaranteed anymore if you redefine
     *             the merge function.
     */
    protected abstract void evalBranch(Branch self);

    public String merge() {
        /*
            If we give a very simple expression such as '50' to the reducer, it will detect that no operation
            needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if (getBranches().size() == 1) {
            LOGGER.debug("Only one branch found. Returns it.");
            return getBranches().get(0).getExpression();
        }

        /*
            Merging the branches from the last one to the first one (this initial expression)
         */
        for (int i = getBranches().size() - 1; i >= 0; i--) {

            Branch self = getBranches().get(i);
            if (!self.canBeCalculated()) {
                LOGGER.info("Not calculable : hasChildren : {} / children have no children : {}",
                        self.hasChildren(), self.doChildrenHaveChildren());
                continue;
            }

            evalBranch(self);

            Branch first = getBranches().get(0);
            if (first.hasAlternativeValue()) {
                if (String.valueOf(first.getAlternativeValue()).equals("Infinity"))
                    throw new ArithmeticException();

                return String.valueOf(first.getAlternativeValue());
            }
        }
        return null;
    }

    @Override
    public final Iterator<Branch> iterator() {
        return branches.iterator();
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public char[] getSplitters() {
        return splitters;
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
            create(this);
        }

        private String deleteUselessBrackets(String exp) {
            while(ExpressionUtils.areEdgesBracketsConnected(exp, false)) {
                exp = exp.substring(1, exp.length()-1);
            }
            return exp;
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

        public String getLeftValue() {
            if(left.hasAlternativeValue())
                return left.getAlternativeValue();
            return left.getExpression();
        }
        public String getRightValue() {
            if(right.hasAlternativeValue())
                return right.getAlternativeValue();
            return right.getExpression();
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
