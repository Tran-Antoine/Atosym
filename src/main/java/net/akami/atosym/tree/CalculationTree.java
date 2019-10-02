package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.handler.sign.BinaryOperationSign;
import net.akami.atosym.core.MaskContext;
import net.akami.atosym.utils.ExpressionUtils;
import net.akami.atosym.utils.FormatterFactory;
import net.akami.atosym.utils.TreeUtils;

/**
 * CalculationTree is an implementation of the {@link BinaryTree} class that manages algebraic calculations, containing
 * the 5 basic binary operations (+ - * / ^) plus the three trigonometric unary operations (sin cos tan). <br>
 *
 * To respect the priority of operations, the {@code begin} method tries to split the expression with (+ -) first, then
 * with (* /), and eventually with (^). Whenever a valid splitter is found, the branch ceases to be analyzed.
 *
 * Note that the class is abstract because the {@link BinaryTree#generate(String)} method needs to be defined, since
 * the generic parameter is not defined. If you look for the most basic calculation tree handling simple branches, see
 * {@link ReducerTree} for further information.
 * @param <T> What kind of branch is handled by the tree
 *
 * @author Antoine Tran
 */
public abstract class CalculationTree<T extends Branch> extends BinaryTree<T> {

    protected final MaskContext context;

    public CalculationTree(String initial, MaskContext context) {
        super(FormatterFactory.addMultiplicationSigns(initial, true), '+', '-', '*', '/', '^', ' ');
        this.context = context;
    }

    /**
     * The overall splitting's behavior of a {@code CalculationTree} is to look for the lowest math signs in terms of
     * priority (+ and -), and to getElement on this way as long as a valid "splitter" is found.
     * @param self the branch itself
     */
    @Override
    protected void begin(T self) {
        for (int i = 0; i < getSplitters().length; i += 2) {
            if(split(self, getSplitters()[i], getSplitters()[i + 1]))
                break;
        }
    }

    @Override
    protected boolean split(T self, char... by) {

        char c1 = by[0];
        char c2 = by[1];
        String expression = self.getExpression();
        /*
            We must go from the end to the beginning. Otherwise, operations' priority is not respected.
            For instance, 2/2*2 = 1. If we go from 0 to exp.getElementsSize() -1, the expression will be divided like this :
            2 |/| 2*2. 2*2 will be calculated first, the final findResult will be 1/2. If we go from exp.getElementsSize() -1
            to 0, the expression will be divided like this :
            2 / 2 |*| 2. 2/2 will be calculated first, the final findResult will be 2.
        */
        for (int i = expression.length() - 1; i >= 0; i--) {


            char c = expression.charAt(i);

            if ((c == c1 || c == c2)) {
                LOGGER.debug("Checking if sign {} at index {} is surrounded in {}", c, i, this);
                boolean bracketsConnected = FormatterFactory.areEdgesBracketsConnected(expression, false);
                LOGGER.info("Brackets connected : {}", bracketsConnected);
                if (!ExpressionUtils.isSurroundedByParentheses(i, expression)) {
                    LOGGER.debug("Found a place to split at index {}, character '{}'", i, c);
                    TreeUtils.createNewBranch(this, self, i, c, bracketsConnected);
                    return true;
                }
            }
        }
        LOGGER.debug("Separations with the operations {} and {} are now done", c1, c2);
        return false;
    }

    @Override
    protected void evalBranch(T self) {
        LOGGER.info("Actual branch : {}", self.getExpression());

        // We are sure that the branch has a left and a right part, because the branch can be calculated
        MathObject left = self.getLeftValue();
        MathObject right = self.getRightValue();

        LOGGER.debug("Left : {}, Right : {}, Operation : {}", left, right, self.getOperation());
        MathObject value = evalValue(left, right, self.getOperation());
        // The findResult is defined as the reduced value of the expression
        LOGGER.info("Successfully calculated the value of " + self.getExpression() + " : " + value);

        self.setReducedValue(value);
    }

    /**
     * Defines what is the merge of the calculation (corresponding to the char given) between left and right. <br>
     *
     * The {@code evalValue} method might be redefined in a sub class of {@code CalculationTree}.
     * @param left the 'a' value of the calculation
     * @param right the 'b' value of the calculation
     * @param sign the operation sign corresponding to a calculation
     * @return an expression corresponding to the evaluation of a and b through the operation matching the sign
     */
    protected MathObject evalValue(MathObject left, MathObject right, char sign) {
        return BinaryOperationSign.getBySign(sign).compute(left, right, context);
    }
}
