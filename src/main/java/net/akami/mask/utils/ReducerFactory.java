package net.akami.mask.utils;

import java.util.List;

import net.akami.mask.math.OperationSign;
import net.akami.mask.math.Tree;
import net.akami.mask.math.Tree.Branch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReducerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReducerFactory.class.getName());
    private static final OperationSign[] OPERATIONS;

    static {
        OPERATIONS = new OperationSign[]{
                OperationSign.SUM, OperationSign.SUBTRACT,
                OperationSign.MULT, OperationSign.DIVIDE,
                OperationSign.POW, OperationSign.NONE
        };
    }

    public static String reduce(String exp) {
        long time = System.nanoTime();

        Tree tree = new Tree();

        // deletes all the spaces
        String localExp = exp.replaceAll("\\s", "");

        tree.new Branch(localExp);
        LOGGER.debug("Initial branch added : {}", tree.getBranches().get(0));

        do {
            // split the expression for each pair of signs.
            for (int i = 0; i < OPERATIONS.length; i += 2) {
                splitBy(tree, OPERATIONS[i].getSign(), OPERATIONS[i + 1].getSign());
            }
        } while(containsParentheses(tree));

        TreeUtils.printBranches(tree);
        String result;
        try {
            result = TreeUtils.mergeBranches(tree);
        } catch (ArithmeticException | NumberFormatException e) {
            if(e instanceof ArithmeticException)
                LOGGER.error("Non solvable mathematical expression given : {}", exp);
            else
                LOGGER.error("Wrong format in the expression {}", exp);
            result = "undefined";
        }

        float deltaTime = (System.nanoTime() - time) / 1000000f;
        LOGGER.info("Expression successfully reduced in {} seconds.", deltaTime);
        return result;
    }
    private static void splitBy(Tree tree, char c1, char c2) {
        List<Branch> branches = tree.getBranches();
        int index = 0;

        while(true) {

            Branch actualBranch = branches.get(index);
            String exp = actualBranch.getExpression();

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
                if(actualBranch.hasChildren())
                    break;

                char c = exp.charAt(i);

                if((c == c1 || c == c2) && !isInsideParentheses(i, actualBranch)) {
                    LOGGER.debug("Found a place to split at index {}, character '{}'",i, c);
                    TreeUtils.createNewBranch(tree, actualBranch, i, c);
                    break;
                }
            }
            if(index + 1 < branches.size()) {
                index++;
            } else {
                break;
            }
        }
        //LOGGER.debug("Separations with the operations {} and {} are now done", c1, c2);
    }

    private static boolean containsParentheses(Tree self) {
        for(Branch branch : self.getBranches()) {
            if(branch.hasChildren())
                continue;
            String exp = branch.getExpression();
            if(exp.contains("(") || exp.contains(")")) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInsideParentheses(int index, Branch self) {

        String exp = self.getExpression();
        if(exp.charAt(0) == '(' && exp.charAt(exp.length()-1) == ')') {
            exp = exp.substring(1, exp.length()-1);
        }
        LOGGER.debug("Checking if sign at index {} is surrounded in {}", index, self);
        int leftParenthesis = 0;

        for(int i = 0; i < exp.length(); i++) {
            if(exp.charAt(i) == '(') {
                leftParenthesis++;
            }

            if(exp.charAt(i) == ')' && i != exp.length()) {
                leftParenthesis--;
            }
            if(leftParenthesis > 0 && i == index) {
                LOGGER.debug("- Indeed surrounded");
                return true;
            }
        }
        LOGGER.debug("Not surrounded");
        return false;
    }

}
