package net.akami.mask.tree;

import net.akami.mask.operation.sign.BinaryOperationSign;
import net.akami.mask.operation.sign.QuaternaryOperationSign;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.util.Optional;

public class DerivativeTree extends CalculationTree<DerivativeBranch> {

    public DerivativeTree(String initial) {
        super(FormatterFactory.formatForCalculations(initial));
    }

    @Override
    public DerivativeBranch generate(String origin) {
        return new DerivativeBranch(origin);
    }

    @Override
    protected void evalBranch(DerivativeBranch self) {
        if(!self.hasChildren()) {
            self.setDerivativeValue(differentiateElement(self.getExpression()));
            return;
        }

        String left = self.getLeftValue(); // either a reduced or the original expression
        String right = self.getRightValue(); // either a reduced or the original expression
        String derLeft = self.getLeft().getDerivativeValue(); // we know it has one
        String derRight = self.getRight().getDerivativeValue(); // we know it has one
        char op = self.getOperation();

        self.setReducedValue(BinaryOperationSign.getBySign(op).compute(left, right));
        self.setDerivativeValue(QuaternaryOperationSign.getBySign(op).compute(left, derLeft, right, derRight));
    }

    public String differentiateElement(String element) {
        if(ExpressionUtils.isANumber(element))
            return "0";

        if(element.length() == 1)
            return "1";

        return MathUtils.diffPow(String.valueOf(element.charAt(0)), null, element.substring(2), null);
    }

    @Override
    public Optional<String> finalResult() {
        DerivativeBranch first = getBranches().get(0);
        if(first.getDerivativeValue() != null) {
            return Optional.of(first.getDerivativeValue());
        }
        return Optional.empty();
    }
}
