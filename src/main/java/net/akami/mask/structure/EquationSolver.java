package net.akami.mask.structure;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EquationSolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquationSolver.class);

    public static String[] solve(List<BiMask> biMasks) {

        MaskOperator op = MaskOperator.begin();
        for(BiMask biMask : biMasks) {
            op.reduce(biMask.left, biMask.left).reduce(biMask.right, biMask.right);
        }

        LOGGER.info("\nNow solving the equations\n");
        Map<Character, String> solutions = new HashMap<>();

        char[] variables = ExpressionUtils.toVariablesType(biMasks).toCharArray();

        for(char v : variables) {
            solutions.put(v, String.valueOf(v));
        }

        for(char var : variables) {
            LOGGER.info("---> Searching for the solution of the variable {}. ", var);
            // In case x = y
            for(char key : solutions.keySet()) {
                if(key != var && solutions.get(key).equals(String.valueOf(var))) {
                    solutions.put(var, String.valueOf(key));
                    continue;
                }
            }
            for(BiMask line : biMasks) {
                if(line.containsVar(var)) {
                    LOGGER.info("Found a line that contains {} : {}", var, line.left+"="+line.right);
                    solutions.put(var, solveLine(line, var, solutions));
                    LOGGER.info("-------> Found solution for {} : {}", var, solutions);
                    break;
                }
            }
        }
        return solutions.values().toArray(new String[0]);
    }

    public static String solveLine(BiMask line, char var, Map<Character, String> solutions) {
        MaskExpression leftMask = line.left;
        MaskExpression rightMask = line.right;
        String leftExp = leftMask.getExpression();
        String rightExp = rightMask.getExpression();

        List<String> leftMonomials = ExpressionUtils.toMonomials(leftExp);
        List<String> rightMonomials = ExpressionUtils.toMonomials(rightExp);

        replaceExistingSolutions(leftMonomials,var, solutions);
        replaceExistingSolutions(rightMonomials, var, solutions);
        rearrange(leftMonomials, rightMonomials, var, true);
        rearrange(rightMonomials, leftMonomials, var, false);

        String numericLeftValue = ExpressionUtils.toNumericValue(MathUtils.sum(leftMonomials));
        return ExpressionUtils.addMultShortcut(MathUtils.divide(MathUtils.sum(rightMonomials), numericLeftValue));
    }

    private static void replaceExistingSolutions(List<String> target, char var, Map<Character, String> solutions) {
        LOGGER.info("Before replacing existing solutions : {}", target);
        MaskOperator op = MaskOperator.begin(MaskExpression.TEMP);
        for(int i = 0; i < target.size(); i++) {
            String monomial = target.get(i);
            if(monomial.startsWith("+") || monomial.startsWith("-")) {
                monomial = monomial.substring(1);
            }
            LOGGER.info("Analyzing monomial {}.", monomial);
            String localVars = ExpressionUtils.toVariablesType(monomial);
            String[] presentSolutions = new String[localVars.length()];
            int j = 0;
            for(char localVar : localVars.toCharArray()) {
                String replacement = solutions.get(localVar);
                // If x = y and we look for transforming y, we don't want to replace x by y...
                if(!replacement.contains(String.valueOf(var))) {
                    presentSolutions[j++] = replacement;
                } else {
                    presentSolutions[j++] = String.valueOf(localVar);
                }
            }
            LOGGER.info("Present solutions : {}", presentSolutions);
            MaskExpression.TEMP.reload(monomial);
            String transformed = op.imageFor(presentSolutions).asExpression();
            target.set(i, transformed);
            LOGGER.info("Successfully replaced {} by {}", monomial, transformed);
        }
        LOGGER.info("After replacing existing solutions : {}", target);
    }

    private static void rearrange(List<String> toRead, List<String> affected, char var, boolean mustContainVar) {

        LOGGER.info("Before rearranging : {} and {}", toRead, affected);
        for (String monomial : toRead) {
            if (monomial.contains(String.valueOf(var)) != mustContainVar) {
                int index = toRead.indexOf(monomial);
                if (monomial.startsWith("+"))
                    monomial = "-" + monomial.substring(1);
                else if (monomial.startsWith("-"))
                    monomial = "+" + monomial.substring(1);
                else
                    monomial = "-" + monomial;
                toRead.set(index, null);
                affected.add(monomial);
            }
        }
        LOGGER.info("After rearranging : {} and {}", toRead, affected);
    }

    public static class BiMask {

        private MaskExpression left;
        private MaskExpression right;

        public BiMask(MaskExpression e1, MaskExpression e2) {
            this.left = e1;
            this.right = e2;
        }

        public MaskExpression getLeft() {
            return left;
        }

        public MaskExpression getRight() {
            return right;
        }

        public boolean containsVar(char var) {
            return left.getExpression().contains(String.valueOf(var))
                    || right.getExpression().contains(String.valueOf(var));
        }
    }
}
