package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.core.MaskSimplifier;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.handler.sign.BinaryOperationSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which will be merged with the {@link MaskSimplifier} class soon.
 */
public class ReducerFactory {

    // private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getNames());
    public static final BinaryOperationSign[] PROCEDURAL_OPERATIONS;

    static {
        PROCEDURAL_OPERATIONS = new BinaryOperationSign[]{
                BinaryOperationSign.SUM, BinaryOperationSign.SUBTRACT,
                BinaryOperationSign.MULT, BinaryOperationSign.DIVIDE,
                BinaryOperationSign.POW, BinaryOperationSign.NONE
        };
    }

    public static MathObject reduce(String exp) {
        return reduce(exp, MaskContext.DEFAULT);
    }

    public static MathObject reduce(String exp, MaskContext context) {
        return null;
    }

    /*
    public static MathObject reduce(String exp, MaskContext context) {
        Objects.requireNonNull(exp, "Cannot reduce a null expression");
        long time = System.nanoTime();

        // deletes all the spaces, adds the necessary '*' and formats trigonometry
        String localExp = FormatterFactory.formatForCalculations(exp, context);
        BinaryTree<Branch> tree = new ReducerTree(localExp, context);
        LOGGER.info("Initial branch added : {}", tree.getBranches().get(0));

        TreeUtils.printBranches(tree);
        LOGGER.debug("Now merging branches");
        MathObject result;
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
    }*/

}
