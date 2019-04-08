package net.akami.mask.tree;

import net.akami.mask.handler.sign.BinaryOperationSign;
import net.akami.mask.handler.sign.QuaternaryOperationSign;
import net.akami.mask.utils.FormatterFactory;

import java.util.Optional;

/**
 * Implementation of the {@link CalculationTree} class. Basically, it adds support for children-less branch evaluation,
 * redefines the {@code finalResult} method and adds support for derivative's calculations for each branch it handles.
 * <br/>
 * The Branch type handled is the {@link DerivativeBranch} type.
 *
 * @author Antoine Tran
 */
public class DerivativeTree extends CalculationTree<DerivativeBranch> {

    private char var;

    /**
     * Available constructor for the derivative tree instance. Defines the initial expression, as well as the variable
     * used to differentiate the expression. It is a very important factor, since :
     *
     * {@code (ax^2)'} equals {@code 2ax} if var = x
     * {@code (ax^2)'} equals {@code x^2} if var = a
     * @param initial the
     * @param var
     */
    public DerivativeTree(String initial, char var) {
        super(FormatterFactory.formatForCalculations(initial));
        this.var = var;
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

        // It can avoid a long execution time. The initial branch does not need a reduced value
        if(getBranches().indexOf(self) != 0)
            self.setReducedValue(BinaryOperationSign.getBySign(op).compute(left, right));
        self.setDerivativeValue(QuaternaryOperationSign.getBySign(op).compute(left, derLeft, right, derRight));
    }

    public String differentiateElement(String element) {

        if(element.equals(String.valueOf(var)))
            return "1";

        return "0";
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
