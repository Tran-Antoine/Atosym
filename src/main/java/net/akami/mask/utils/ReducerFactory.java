package net.akami.mask.utils;

import net.akami.mask.operation.sign.BinaryOperationSign;
import net.akami.mask.math.BinaryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getName());
    private static final StringBuilder BUILDER = new StringBuilder();
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

        BinaryTree tree = new BinaryTree();

        // deletes all the spaces, adds the necessary '*'
        String localExp = FormatterFactory.formatForCalculations(exp);

        tree.new Branch(localExp);
        LOGGER.info("Initial branch added : {}", tree.getBranches().get(0));

        TreeUtils.printBranches(tree);
        LOGGER.debug("Now merging branches");
        String result;
        try {
            result = TreeUtils.mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Wrong inFormat in the expression {}", exp);
            result = "undefined";
            e.printStackTrace();
        }

        float deltaTime = (System.nanoTime() - time) / 1000000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return FormatterFactory.formatForVisual(result);
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

}
