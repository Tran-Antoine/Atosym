package net.akami.mask.structure;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.ReducerFactory;
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
            if(!solveVariable(biMasks, var, solutions, 0)) {
                LOGGER.info("Couldn't find a line that does not contain an uncalculated variable");
                if(!solveVariable(biMasks, var, solutions, 1)) {
                    LOGGER.info("Couldn't find a line that has not already been used");
                    solveVariable(biMasks, var, solutions, 2);
                }
            }
        }
        return solutions.values().toArray(new String[0]);
    }

    /**
     * @return whether the variable has been calculated or not
     */
    private static boolean solveVariable(List<BiMask> lines, char var, Map<Character, String> solutions, int mode) {

        for(BiMask line : lines) {
            if(line.containsVar(var)) {
                if(mode == 0 && (containsUncalculatedVar(line, var, solutions) || line.hasBeenUsed())) {
                    continue;
                }
                if(mode == 1 && line.hasBeenUsed()) {
                    continue;
                }
                line.setUsed(true);
                LOGGER.info("Found a line that contains {} : {}", var, line.left+"="+line.right);
                String solution = solveLine(line, var, solutions);
                solutions.put(var, solution);
                replaceSolutionInOthers(var, solution, solutions);
                LOGGER.info("-------> Found solution for {} : {}", var, solutions);
                return true;
            }
        }
        return false;
    }

    private static void replaceSolutionInOthers(char var, String varSolution, Map<Character, String> solutions) {

        MaskOperator op = MaskOperator.begin();
        for(char key : solutions.keySet()) {
            if(var == key) continue;

            String keySolution = solutions.get(key);
            // TODO : use imageFor method
            if(keySolution.contains(String.valueOf(var))) {
                String newKeySolution = ReducerFactory.reduce(op.replace(var, varSolution, keySolution));
                solutions.put(key, newKeySolution);
                LOGGER.info("Previous solution of {} contained {}. {} was replaced by {}", key, var, keySolution, newKeySolution);
            }
        }
    }

    private static boolean containsUncalculatedVar(BiMask line, char var, Map<Character, String> solutions) {
        for(char key : solutions.keySet()) {
            if(key != var && line.containsVar(key)) {
                String solution = solutions.get(key);
                if(solution.equals(String.valueOf(key))) {
                    return true;
                }
            }
        }
        return false;
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
        String rightValue = MathUtils.sum(rightMonomials);
        LOGGER.info("Final step : dividing {} by {}", rightValue, numericLeftValue);
        return ExpressionUtils.addMultShortcut(MathUtils.divide(rightValue, numericLeftValue));
    }

    private static void replaceExistingSolutions(List<String> target, char var, Map<Character, String> solutions) {
        LOGGER.info("Before replacing existing solutions : {}", target);
        List<String> additionalSolutions = new ArrayList<>();
        MaskOperator op = MaskOperator.begin(MaskExpression.TEMP);
        for(int i = 0; i < target.size(); i++) {
            String monomial = target.get(i);
            LOGGER.info("Analyzing monomial {}.", monomial);
            String localVars = ExpressionUtils.toVariablesType(monomial);
            String[] presentSolutions = new String[localVars.length()];
            int j = 0;
            // Building the values array
            for(char localVar : localVars.toCharArray()) {
                String replacement = solutions.get(localVar);

                // If x = y and we look for transforming y, we don't want to replace x by y...
                // TODO : condition should be !replacement.isASimpleMultOfAAndX
                if(!replacement.equals(String.valueOf(var))) {
                    presentSolutions[j++] = replacement;
                } else {
                    presentSolutions[j++] = String.valueOf(localVar);
                }
            }
            LOGGER.info("Calculating the image of {}, with solutions : {}",monomial, presentSolutions);
            MaskExpression.TEMP.reload(monomial);
            String transformed = op.imageFor(presentSolutions).asExpression();
            target.set(i, null);
            // If the solution if x+1, we can't add "x+1", we must add "+x" and "+1"
            LOGGER.error("For {} found {}, added {}",monomial, transformed, ExpressionUtils.toMonomials(transformed));
            additionalSolutions.addAll(ExpressionUtils.toMonomials(transformed));
            LOGGER.info("Successfully replaced {} by {}", monomial, transformed);
        }
        target.addAll(additionalSolutions);
        LOGGER.info("--> After replacing existing solutions : {}", target);
    }

    private static void rearrange(List<String> toRead, List<String> affected, char var, boolean mustContainVar) {

        LOGGER.info("Before rearranging : {} and {}", toRead, affected);
        for (String monomial : toRead) {
            if(monomial == null) continue;

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
        private boolean used = false;

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

        public boolean hasBeenUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }
    }
}