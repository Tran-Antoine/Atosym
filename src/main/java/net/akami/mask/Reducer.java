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

        String result;
        try {
            result = mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Wrong format in the expression {}", exp);
            result = "undefined";
        }

        float deltaTime = (System.nanoTime() - time) / 1000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return new RestCalculation(result);
    }
    public static void splitBy(Tree tree, char c1, char c2) {

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
                    createNewBranch(tree, actual, exp, i, c);
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

    private static void createNewBranch(Tree tree, Branch actual, String exp, int index, char operation) {

        String left = exp.substring(0, index);
        String right = exp.substring(index+1);
        actual.setLeft(tree.new Branch(left));
        actual.setRight(tree.new Branch(right));
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
            float value = Operation.getBySign(branch.getOperation()).compute(left, right);
            // The result is defined as the reduced value of the expression
            LOGGER.debug("Successfully calculated the value of "+branch.getExpression()+" : "+value);

            branch.setReducedValue(value);
            branch.setLeft(null);
            branch.setRight(null);

            Branch first = tree.getBranches().get(0);
            if(first.isReduced()) {
                if(String.valueOf(first.getReducedValue()).equals("Infinity"))
                    throw new ArithmeticException();

                return String.valueOf(first.getReducedValue());
            }
        }
        return null;
    }
}
