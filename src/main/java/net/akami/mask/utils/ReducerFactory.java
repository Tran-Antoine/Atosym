package net.akami.mask.utils;

import net.akami.mask.operation.OperationSign;
import net.akami.mask.math.BinaryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getName());
    private static final StringBuilder BUILDER = new StringBuilder();
    public static final OperationSign[] PROCEDURAL_OPERATIONS;

    static {
        PROCEDURAL_OPERATIONS = new OperationSign[]{
                OperationSign.SUM, OperationSign.SUBTRACT,
                OperationSign.MULT, OperationSign.DIVIDE,
                OperationSign.POW, OperationSign.NONE
        };
    }

    public static String reduce(String exp) {
        long time = System.nanoTime();

        BinaryTree tree = new BinaryTree();

        // deletes all the spaces, adds the necessary '*'
        String localExp = ExpressionUtils.cancelMultShortcut(exp.replaceAll("\\s", ""));

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
        return ExpressionUtils.addMultShortcut(result);
    }

    private static void clearBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

}
