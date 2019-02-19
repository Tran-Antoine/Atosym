package net.akami.mask.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree is an object that contains a list of branches.
 */
public class Tree {

    private List<Branch> branches;

    public Tree() {
        this.branches = new ArrayList<>();
    }

    public List<Branch> getBranches() { return branches; }

    public class Branch {

        private Branch left;
        private Branch right;
        private char operation;
        private String expression;
        private boolean reduced;
        private String reducedValue;

        public Branch(String expression) {
            //boolean surrounded = expression.charAt(0) == '(' && expression.charAt(expression.length()-1) == ')';
            //this.expression = surrounded ? expression.substring(1, expression.length()-1) : expression;
            this.expression = expression;
            reduced = false;
            branches.add(this);
        }

        public Branch getLeft()         { return left;         }
        public Branch getRight()        { return right;        }
        public char getOperation()      { return operation;    }
        public String getExpression()   { return expression;   }
        public String getReducedValue() { return reducedValue; }
        public boolean isReduced()      { return reduced;      }

        public void setOperation(char operation) { this.operation = operation; }
        public void setLeft(Branch left)         { this.left = left;           }
        public void setRight(Branch right)       { this.right = right;         }
        public void setReducedValue(String value){
            this.reducedValue = value;
            reduced = true;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Branch) {
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
            if(hasChildren())
                return left.hasChildren() || right.hasChildren();
            return false;
        }

        public boolean canBeCalculated() {
            if(!hasChildren()) {
                return false;
            }
            return !doChildrenHaveChildren();
        }
    }
}
