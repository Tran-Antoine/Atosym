package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;

public class FormatterTree extends CalculationTree<Branch> {

    public FormatterTree(String initial) {
        super(FormatterFactory.addMultiplicationSigns(ExpressionUtils.removeEdgeBrackets(initial), true));
    }

    @Override
    protected Branch generate(String origin) {
        return new Branch(origin);
    }

    @Override
    protected void evalBranch(Branch self) {

        String left = self.getLeftValue();
        String right = self.getRightValue();

        if(evalTrigonometry(self, left, right)) return;

        if(ExpressionUtils.hasHigherPriority(String.valueOf(self.getOperation()), right))
            right = addRequiredBrackets(right);

        char operation = self.getOperation();

        if(operation == '*') {
            self.setReducedValue(left + right);
        } else if(operation == '-' && left.equals("0")){
            self.setReducedValue(operation + right);
        } else {
            self.setReducedValue(left + operation + right);
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
                    self.setReducedValue("sin("+value+")");
                    break;
                case '#':
                    self.setReducedValue("cos("+value+")");
                    break;
                case '§':
                    self.setReducedValue("tan("+value+")");
            }
            return true;
        }
        return false;
    }
}
