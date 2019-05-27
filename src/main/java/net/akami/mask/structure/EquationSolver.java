package net.akami.mask.structure;

import net.akami.mask.core.*;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.handler.Adder;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.ReducerFactory;
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
            biMasks.add(new BiMask(new Mask(sides[0]), new Mask(sides[1])));
        }
        return biMasks;
    }

    public static Map<Character, String> solve(List<BiMask> biMasks) {

        List<BiExpression> biExpressions = new ArrayList<>(biMasks.size());

        for(BiMask biMask : biMasks) {
            Expression left = ReducerFactory.reduce(biMask.left.getExpression());
            Expression right = ReducerFactory.reduce(biMask.right.getExpression());
            biExpressions.add(new BiExpression(left, right));
        }

        return solve(biExpressions, biMasks);

    }

    public static Map<Character, String> solve(List<BiExpression> biExpressions, List<BiMask> biMasks) {

        for(BiExpression biExpression : biExpressions) {
            if(biExpression.left.getMaximalPower() > 1 || biExpression.right.getMaximalPower() > 1)
                throw new IllegalArgumentException("Cannot solve 2nd degree (or more) equations yet.");
        }
        LOGGER.info("\nNow solving the equations\n");
        Map<Character, String> solutions = defaultSolutions(biMasks);


            for (char var : solutions.keySet()) {
                LOGGER.info("Searching for the solution of the variable {}. ", var);
                // In case x = y
                for (char key : solutions.keySet()) {
                    if (key != var && solutions.get(key).equals(String.valueOf(var))) {
                        solutions.put(var, String.valueOf(key));
                    }
                }

                if (!solveVariable(biExpressions, biMasks, var, solutions, 0)) {
                    LOGGER.info("Couldn't find a line that does not contain an uncalculated variable");

                    if (!solveVariable(biExpressions, biMasks, var, solutions, 1)) {
                        LOGGER.info("Couldn't find a line that has not already been used");
                        solveVariable(biExpressions, biMasks, var, solutions, 2);
                    }


                }


            }

        return solutions;
    }
    /**
     * @return whether the variable has been calculated or not
     */
    private static boolean solveVariable(List<BiExpression> expressions, List<BiMask> lines, char var, Map<Character, String> solutions, int mode) {

        for(int i = 0; i < lines.size(); i++) {
            BiMask line = lines.get(i);
            BiExpression expression = expressions.get(i);
            if(line.containsVar(var)) {
                if(mode == 0 && (containsUncalculatedVar(line, var, solutions) || line.hasBeenUsed())) {
                    continue;
                }
                if(mode == 1 && line.hasBeenUsed()) {
                    continue;
                }

                line.setUsed(true);
                LOGGER.info("Found a line that contains {} : {}", var, line.left + "=" + line.right);
                String solution = solveLine(expression, var, solutions);
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

                Mask temp = Mask.TEMP;
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

    public static String solveLine(BiExpression line, char var, Map<Character, String> solutions) {
        Expression left = line.left;
        Expression right = line.right;

        List<Monomial> leftMonomials = new ArrayList<>(left.getElements());
        List<Monomial> rightMonomials = new ArrayList<>(right.getElements());


        replaceExistingSolutions(leftMonomials, var, solutions);
        replaceExistingSolutions(rightMonomials, var, solutions);

        rearrange(leftMonomials, rightMonomials, var, true);
        rearrange(rightMonomials, leftMonomials, var, false);

        Adder adder = MaskContext.DEFAULT.getBinaryOperation(Adder.class);
        float numericLeftValue = adder.monomialSum(leftMonomials).get(0).getNumericValue();
        Expression rightValue = adder.monomialSum(rightMonomials);
        LOGGER.info("Final step : dividing {} by {}", rightValue, numericLeftValue);
        return FormatterFactory.removeMultiplicationSigns(
                MathUtils.divide(rightValue, Expression.of(numericLeftValue)).toString());
    }

    private static void replaceExistingSolutions(List<Monomial> target, char var, Map<Character, String> solutions) {
        LOGGER.info("Before replacing existing solutions : {}", target);
        List<Monomial> additionalSolutions = new ArrayList<>();

        for(int i = 0; i < target.size(); i++) {
            Monomial monomial = target.get(i);
            if(monomial == null) continue;
            LOGGER.info("Analyzing monomial {}.", monomial);
            LOGGER.info("Calculating the image of {}, with solutions : {}",monomial, solutions);
            Mask.TEMP.reload(monomial.getExpression());
            handler.begin(Mask.TEMP);


            Map<Character, String> copy = new HashMap<>(solutions);
            copy.remove(var);

            String transformed = handler.compute(MaskImageCalculator.class, null, copy).asExpression();

            target.set(i, null);
            // If the solution if x+1, we can't add "x+1", we must add "+x" and "+1"
            additionalSolutions.addAll(ReducerFactory.reduce(transformed).getElements());
            LOGGER.info("Successfully replaced {} by {}", monomial, transformed);

        }
        target.addAll(additionalSolutions);
        LOGGER.info("--> After replacing existing solutions : {}", target);
    }

    private static void rearrange(List<Monomial> toRead, List<Monomial> affected, char var, boolean mustContainVar) {

        LOGGER.info("Before rearranging : {} and {}", toRead, affected);
        for (Monomial monomial : toRead) {
            if(monomial == null) continue;

            String monomialExp = monomial.getExpression();
            if (monomialExp.contains(String.valueOf(var)) != mustContainVar) {
                int index = toRead.indexOf(monomial);
                if (monomialExp.startsWith("+"))
                    monomialExp = "-" + monomialExp.substring(1);
                else if (monomialExp.startsWith("-"))
                    monomialExp = "+" + monomialExp.substring(1);
                else
                    monomialExp = "-" + monomialExp;
                toRead.set(index, null);
                affected.add(ReducerFactory.reduce(monomialExp).get(0));
            }
        }
        LOGGER.info("After rearranging : {} and {}", toRead, affected);
    }

    private static Map<Character, String> defaultSolutions(List<BiMask> biMasks) {
        Map<Character, String> solutions = new HashMap<>();

        char[] variables = ExpressionUtils.toVariablesType(biMasks).toCharArray();

        for(char v : variables) {
            solutions.put(v, String.valueOf(v));
        }
        return solutions;
    }

    public static class BiMask {

        private Mask left;
        private Mask right;
        private boolean used = false;

        public BiMask(Mask e1, Mask e2) {
            this.left = e1;
            this.right = e2;
        }

        public Mask getLeft() {
            return left;
        }

        public Mask getRight() {
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

    public static class BiExpression {

        private Expression left;
        private Expression right;

        public BiExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }
    }
}
