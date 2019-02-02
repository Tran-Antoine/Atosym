package net.akami.mask.core;

import java.util.List;
import net.akami.mask.core.Tree.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reducer.class.getName());

    static {
        new Operation('+').setFunction((a, b) -> a+b);
        new Operation('-').setFunction((a, b) -> a-b);
        new Operation('*').setFunction(((a, b) -> a*b));
        new Operation('/').setFunction((a, b) -> a/b);
    }

    private static final char[] OPERATIONS = {' ', ' ', '+', '-', '*', '/'};

    public static String reduce(String exp) {
        long time = System.nanoTime();
        // deletes all the spaces
        String localExp = exp.replaceAll("\\s", "");
        Tree tree = new Tree();
        tree.new Branch(localExp, null);

        for(int i = 2; i<OPERATIONS.length; i+=2) {
            reduceBy(OPERATIONS[i], OPERATIONS[i+1], OPERATIONS[i-1], OPERATIONS[i-2], tree);
        }
        String result;
        try {
            result = mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Number present in the expression {} too high", exp);
            result = "undefined";
        }
        float deltaTime = (System.nanoTime() - time) / 1000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return result;
    }
    public static void reduceBy(char c1, char c2, char p1, char p2, Tree tree) {

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
                if(exp.contains(""+p1) || exp.contains(""+p2))
                    break;

                char c = exp.charAt(i);

                if(c == c1 || c == c2) {
                    createNewBranch(exp, i, c, actual, tree);
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

    private static void createNewBranch(String exp, int index, char operation, Branch actual, Tree tree) {

        String left = exp.substring(0, index);
        String right = exp.substring(index+1);
        actual.setLeft(tree.new Branch(left, actual));
        actual.setRight(tree.new Branch(right, actual));
        actual.setOperation(operation);
    }

    private static String mergeBranches(Tree tree) throws ArithmeticException {

        /*
            If we give a very simple expression such as '50' to the reducer, it will detect that no operation
            needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if(tree.getBranches().size() == 1) {
            return tree.getBranches().get(0).getExpression();
        }

        /*
            Merging the branches from the last one to the first one (this initial expression)
         */
        for (int i = tree.getBranches().size() -1; i >= 0; i--) {

            Branch branch = tree.getBranches().get(i);
            LOGGER.debug("Actual branch : {}\n", branch.getExpression());

            if(!branch.canBeCalculated()) {
                LOGGER.debug("Not calculable.");
                continue;
            }
            // We are sure that the branch has a left and a right part, because the branch can be calculated
            String left = branch.getLeft().getExpression();
            String right = branch.getRight().getExpression();

            /*  We need to check whether the left/right part is already reduced as a simple number, or whether
                a reduced form has been calculated
            */
            if(branch.getLeft().isReduced()) {
                left = String.valueOf(branch.getLeft().getReducedValue());
            }
            if(branch.getRight().isReduced()) {
                right = String.valueOf(branch.getRight().getReducedValue());
            }
            int value = Operation.getByCharacter(branch.getOperation()).compute(left, right);
            // The result is defined as the reduced value of the expression
            LOGGER.debug("Successfully calculated the value of "+branch.getExpression()+" : "+value);

            branch.setReducedValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            if(tree.getBranches().get(0).isReduced()) {
                return String.valueOf(tree.getBranches().get(0).getReducedValue());
            }
        }
        return null;
    }
}
