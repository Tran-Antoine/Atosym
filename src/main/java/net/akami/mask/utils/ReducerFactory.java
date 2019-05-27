package net.akami.mask.utils;

import net.akami.mask.expression.Expression;
import net.akami.mask.core.MaskContext;
import net.akami.mask.handler.sign.BinaryOperationSign;
import net.akami.mask.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Class which will be merged with the {@link net.akami.mask.core.MaskReducer} class soon.
 */
public class ReducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getName());
    public static final BinaryOperationSign[] PROCEDURAL_OPERATIONS;

    static {
        PROCEDURAL_OPERATIONS = new BinaryOperationSign[]{
                BinaryOperationSign.SUM, BinaryOperationSign.SUBTRACT,
                BinaryOperationSign.MULT, BinaryOperationSign.DIVIDE,
                BinaryOperationSign.POW, BinaryOperationSign.NONE
        };
    }

    public static Expression reduce(String exp) {
        return reduce(exp, MaskContext.DEFAULT);
    }

    public static Expression reduce(String exp, MaskContext context) {
        Objects.requireNonNull(exp, "Cannot reduce a null expression");
        long time = System.nanoTime();

        // deletes all the spaces, adds the necessary '*' and formats trigonometry
        String localExp = FormatterFactory.formatForCalculations(exp);
        BinaryTree<Branch> tree = new ReducerTree(localExp, context);
        LOGGER.info("Initial branch added : {}", tree.getBranches().get(0));

        TreeUtils.printBranches(tree);
        LOGGER.debug("Now merging branches");
        Expression result;
        try {
            result = tree.merge();
        } catch (ArithmeticException | NumberFormatException e) {
            e.printStackTrace();
            if(e instanceof ArithmeticException) {
                throw new IllegalArgumentException("Non solvable mathematical expression given : "+ exp);
            } else {
                throw new IllegalArgumentException("Wrong inFormat in the expression "+ exp);
            }
        }

        float deltaTime = (System.nanoTime() - time) / 1000000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return result;
    }
}
