package net.akami.mask;

import java.util.List;

import net.akami.mask.Tree.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reducer.class.getName());
    private static final Operation[] OPERATIONS;

    static {
        OPERATIONS = new Operation[]{
                Operation.SUM, Operation.SUBTRACT,
                Operation.MULT, Operation.DIVIDE,
                Operation.POW, Operation.NONE
        };
    }

    public static RestCalculation reduce(String exp) {
        long time = System.nanoTime();

        Tree tree = new Tree();

        // deletes all the spaces
        String localExp = exp.replaceAll("\\s", "");
        tree.new Branch(localExp);

        // split the expression for each pair of signs.
        for(int i = 0; i < OPERATIONS.length; i+=2) {
            splitBy(tree, OPERATIONS[i].getSign(), OPERATIONS[i+1].getSign());
        }

        RestCalculation result;
        try {
            result = TreeUtils.mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Wrong format in the expression {}", exp);
            result = new RestCalculation("undefined");
        }

        float deltaTime = (System.nanoTime() - time) / 1000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return result;
    }
    private static void splitBy(Tree tree, char c1, char c2) {
        List<Branch> branches = tree.getBranches();
        int index = 0;

        while(true) {

            Branch actual = branches.get(index);
            String exp = actual.getExpression();

            /*
                We must go from the end to the beginning. Otherwise, operations' priority is not respected.
                For instance, 2/2*2 = 1. If we go from 0 to exp.length() -1, the expression will be divided like this :
                2 |/| 2*2. 2*2 will be calculated first, the final result will be 1/2. If we go from exp.length() -1
                to 0, the expression will be divided like this :
                2 / 2 |*| 2. 2/2 will be calculated first, the final result will be 2.
             */
            for(int i = exp.length()-1; i >= 0; i--) {
                /*
                    Avoids splits with the roots parts. For instance, when splitting by '+' and '-' for the
                    expression '5+3*2', the result will be 5+3*2, 5, and 3*2. When splitting by '*' and '/', we don't
                    want '5+3*2' to be split, because it contains one of the previous operations (+).
                 */
                if(actual.hasChildren())
                    break;

                char c = exp.charAt(i);

                if(c == c1 || c == c2) {
                    TreeUtils.createNewBranch(tree, actual, exp, i, c);
                    break;
                }
            }
            if(index + 1 < branches.size()) {
                index++;
            } else {
                break;
            }
        }
        LOGGER.debug("Separations with the operations {} and {} are done", c1, c2);
    }

}
