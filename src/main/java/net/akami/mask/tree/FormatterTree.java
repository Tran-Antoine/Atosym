package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;

public class FormatterTree extends CalculationTree {

    public FormatterTree(String initial) {
        super(FormatterFactory.addMultiplicationSigns(ExpressionUtils.removeEdgeBrackets(initial), true));
    }

    @Override
    protected void evalBranch(Branch self) {
        String left = self.getLeftValue();
        String right = self.getRightValue();

        self.setLeft(null);
        self.setRight(null);

        if(evalTrigonometry(self, left, right)) return;

        if(ExpressionUtils.hasHigherPriority(String.valueOf(self.getOperation()), right))
            right = addRequiredBrackets(right);

        char operation = self.getOperation();

        if(operation == '*') {
            self.setAlternativeValue(left + right);
        } else if(operation == '-' && left.equals("0")){
            self.setAlternativeValue(operation + right);
        } else {
            self.setAlternativeValue(left + operation + right);
        }
    }

    private String addRequiredBrackets(String self) {
        if(ExpressionUtils.isReduced(self))
            return self;
        return "(" + self + ")";
    }

    private boolean evalTrigonometry(Branch self, String left, String right) {
        char trigonometricSign = '$';
        String value = null;

        if(ExpressionUtils.isTrigonometricShortcut(left)) {
            trigonometricSign = left.charAt(0);
            value = right;
        } else if(ExpressionUtils.isTrigonometricShortcut(right)) {
            trigonometricSign = right.charAt(0);
            value = left;
        }

        if(trigonometricSign != '$') {
            switch (trigonometricSign) {
                case '@':
                    self.setAlternativeValue("sin("+value+")");
                    break;
                case '#':
                    self.setAlternativeValue("cos("+value+")");
                    break;
                case '§':
                    self.setAlternativeValue("tan("+value+")");
            }
            return true;
        }
        return false;
    }

    /*@Override
    protected void split(Branch self, char... by) {

        if(self.hasChildren())
            return;

        char splitter = by[0]; // '*'
        String exp = self.getExpression();

        for (int i = exp.length() - 1; i >= 0; i--) {
            if(exp.charAt(i) == splitter) {
                boolean bracketsConnected = ExpressionUtils.areEdgesBracketsConnected(exp, true);
                if(!ExpressionUtils.isSurroundedByParentheses(i, exp)) {
                    TreeUtils.createNewBranch(this, self, i, splitter, bracketsConnected);
                    return;
                }
            }
        }
    }*/
}
