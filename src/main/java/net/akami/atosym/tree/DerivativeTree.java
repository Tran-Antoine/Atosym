package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.utils.FormatterFactory;

/**
 * Implementation of the {@link CalculationTree} class. Basically, it adds support for children-less branch evaluation,
 * redefines the {@code finalResult} method and adds support for derivative's calculations for each branch it handles.
 * <br>
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
     * @param var the variable that is not constant
     * @param context the context that the tree belongs to
     */
    public DerivativeTree(String initial, char var, MaskContext context) {
        super(FormatterFactory.formatForCalculations(initial, context), context);
        this.var = var;
    }

    @Override
    public DerivativeBranch generate(String origin) {
        return new DerivativeBranch(origin);
    }

    @Override
    protected void evalBranch(DerivativeBranch self) {
        /*if(!self.hasChildren()) {
            self.setReducedValue(null); // TODO
            //self.setDerivativeValue(new Expression(differentiateElement(self.getExpression())));
            return;
        }

        MathObject left = self.getLeftValue(); // either a reduced or the original expression
        MathObject right = self.getRightValue(); // either a reduced or the original expression
        MathObject derLeft = self.getLeft().getDerivativeValue(); // we know it has one
        MathObject derRight = self.getRight().getDerivativeValue(); // we know it has one
        char op = self.getOperation();

        // It can avoid a long execution time. The initial branch does not need a reduced value
        if(getBranches().indexOf(self) != 0) {
            self.setReducedValue(BinaryOperationSign.getBySign(op).compute(left, right, super.context));
        }
        self.setDerivativeValue(QuaternaryOperationSign.getBySign(op).compute(left, derLeft, right, derRight));*/
    }

    public String differentiateElement(String element) {

        if(element.equals(String.valueOf(var)))
            return "1";

        return "0";
    }
}
