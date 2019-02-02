package net.akami.mask;

import java.util.ArrayList;
import java.util.List;

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
        private float reducedValue;

        public Branch(String expression) {
            this.expression = expression;
            reduced = false;
            branches.add(this);
        }

        public Branch getLeft()        { return left;         }
        public Branch getRight()       { return right;        }
        public char getOperation()     { return operation;    }
        public String getExpression()  { return expression;   }
        public float getReducedValue() { return reducedValue; }
        public boolean isReduced()     { return reduced;      }

        public void setOperation(char operation) { this.operation = operation; }
        public void setLeft(Branch left)         { this.left = left;           }
        public void setRight(Branch right)       { this.right = right;         }
        public void setExpression(String exp)    { this.expression = exp;      }
        public void setReducedValue(float value) {
            this.reducedValue = value;
            reduced = true;
        }
        public void delete()  {
            branches.remove(this);

            if(hasChildren()) {
                left.delete();
                right.delete();
            }
        }

        @Override
        public boolean equals(Object obj) {
            if(obj != null && obj instanceof Branch) {
                return expression.equals(((Branch) obj).expression);
            }
            return false;
        }

        public boolean hasChildren() {
            return left != null || right != null;
        }

        public boolean canBeCalculated() {
            if(!hasChildren()) {
                return false;
            }
            return !(left.hasChildren() || right.hasChildren());
        }
    }
}
