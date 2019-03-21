package net.akami.mask.utils;

import net.akami.mask.operation.sign.BinaryOperationSign;
import net.akami.mask.tree.BinaryTree;
import net.akami.mask.tree.Branch;
import net.akami.mask.tree.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static String reduce(String exp) {
        long time = System.nanoTime();

        // deletes all the spaces, adds the necessary '*' and formats trigonometry
        String localExp = FormatterFactory.formatForCalculations(exp);
        BinaryTree<Branch> tree = new Reducer(localExp);

        LOGGER.info("Initial branch added : {}", tree.getBranches().get(0));

        TreeUtils.printBranches(tree);
        LOGGER.debug("Now merging branches");
        String result;
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
        return FormatterFactory.formatForVisual(result);
    }
}
