package net.akami.mask.structure;

import net.akami.mask.core.MaskExpression;
import net.akami.mask.core.MaskOperatorHandler;
import net.akami.mask.core.MaskImageCalculator;
import net.akami.mask.core.MaskReducer;
import net.akami.mask.utils.ExpressionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EquationSolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquationSolver.class);
    private static final MaskOperatorHandler handler = new MaskOperatorHandler();

    public static List<BiMask> build(String... lines) {
        List<BiMask> biMasks = new ArrayList<>();
        for(String line : lines) {
            String[] sides = line.split("=");
            if(sides.length != 2) throw new IllegalArgumentException("Invalid line given (0 or more than 1 '=' found");
            biMasks.add(new BiMask(new MaskExpression(sides[0]), new MaskExpression(sides[1])));
        }
        return biMasks;
    }

    public static Map<Character, String> solve(List<BiMask> biMasks) {

        for(BiMask biMask : biMasks) {
            if(ExpressionUtils.getMaximalNumericPower(biMask.left.getExpression()+'='+biMask.right.getExpression()) > 1)
                throw new IllegalStateException("Cannot solve squared or more equations");

            handler.compute(MaskReducer.class, biMask.left, biMask.left, null)
                    .compute(MaskReducer.class, biMask.right, biMask.right, null);
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
        return solutions;
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

        for(char key : solutions.keySet()) {
            if(var == key) continue;

            String keySolution = solutions.get(key);
            if(keySolution.contains(String.valueOf(var))) {
                Map<Character, String> image = new HashMap<>();
                image.put(var, varSolution);

                MaskExpression temp = MaskExpression.TEMP;
                temp.reload(keySolution);
                String newKeySolution = handler.compute(MaskImageCalculator.class, temp, null, image).asExpression();
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

        String numericLeftValue = null;//ExpressionUtils.toNumericValue(MathUtils.sum(leftMonomials));
        String rightValue = null;//MathUtils.sum(rightMonomials);
        LOGGER.info("Final step : dividing {} by {}", rightValue, numericLeftValue);
        return null;//FormatterFactory.removeMultiplicationSigns(MathUtils.divide(rightValue, numericLeftValue));
    }

    private static void replaceExistingSolutions(List<String> target, char var, Map<Character, String> solutions) {
        LOGGER.info("Before replacing existing solutions : {}", target);
        List<String> additionalSolutions = new ArrayList<>();

        for(int i = 0; i < target.size(); i++) {
            String monomial = target.get(i);
            LOGGER.info("Analyzing monomial {}.", monomial);
            String localVars = ExpressionUtils.toVariablesType(monomial);

            LOGGER.info("Calculating the image of {}, with solutions : {}",monomial, solutions);
            MaskExpression.TEMP.reload(monomial);
            handler.begin(MaskExpression.TEMP);

            Map<Character, String> copy = new HashMap<>(solutions);
            copy.remove(var);

            String transformed = handler.compute(MaskImageCalculator.class, null, copy).asExpression();
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
