package net.akami.mask.operation;

import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sum extends BinaryOperationHandler {

    private static final Sum INSTANCE = new Sum();

    @Override
    protected String operate(String a, String b) {
        LOGGER.info("Sum process of {} |+| {}: \n", a, b);
        List<String> monomials = ExpressionUtils.toMonomials(a);
        monomials.addAll(ExpressionUtils.toMonomials(b));
        LOGGER.info("Monomials : {}", monomials);
        return monomialSum(monomials);
    }

    public String monomialSum(List<String> monomials) {

        List<String> finalMonomials = new ArrayList<>();

        for (int i = 0; i < monomials.size(); i++) {
            String part = monomials.get(i);
            if (part == null || part.isEmpty())
                continue;
            fillMonomialList(part, i, monomials, finalMonomials);
        }

        // All the monomials that couldn't be calculated because their unknown part was unique are eventually added
        finalMonomials.addAll(monomials);
        clearBuilder();
        for (String rest : finalMonomials) {
            if (rest == null)
                continue;

            if (rest.startsWith("+") || rest.startsWith("-")) {
                BUILDER.append(rest);
            } else {
                BUILDER.append("+" + rest);
            }
        }
        String result = BUILDER.toString();
        LOGGER.info("- Raw result of monomialSum / subtraction : {}", result);
        return result.startsWith("+") ? result.substring(1) : result;
    }

    // TODO : Stop using this map
    private void fillMonomialList(String monomial, int i, List<String> initialMonomials, List<String> finalMonomials) {
        String vars = ExpressionUtils.toVariables(monomial);
        LOGGER.debug("Analyzing monomial {} : {}, found \"{}\" as variables", i, monomial, vars);
        // Adding all the "additionable" parts to the map, with their value and their index
        Map<BigDecimal, Integer> compatibleParts = new HashMap<>();

        for (int j = 0; j < initialMonomials.size(); j++) {
            // We don't want to add the part itself
            if (i == j)
                continue;

            String part2 = initialMonomials.get(j);
            if (part2 == null)
                continue;

            // If the unknown part is similar, we can add them
            if (ExpressionUtils.toVariables(part2).equals(vars)) {
                BigDecimal toAdd = new BigDecimal(ExpressionUtils.toNumericValue(part2));
                if (compatibleParts.containsKey(toAdd)) {
                    LOGGER.info("Found copy in the map. Doubling the original.");
                    int index = compatibleParts.get(toAdd);
                    compatibleParts.remove(toAdd, index);
                    compatibleParts.put(toAdd.multiply(new BigDecimal("2")), index);
                } else {
                    compatibleParts.put(toAdd, j);
                }
            }
        }
        replaceMonomialsByResult(monomial, vars, i, compatibleParts, initialMonomials, finalMonomials);
    }


    /**
     * Calculates the monomialSum of all numeric values of the monomials having vars as their unknown part, then
     * removes the calculated values from the initial list, and adds the result into the final list.
     *
     * Example :
     *
     * 2x + 2x + 3x
     * -> Compatible unknown part : x. Hence, initialMonomial is 2x, others contains 2x and 3x, it removes
     * the three monomials to the initial list, and adds 7x to the final list
     * @param initialMonomial
     * @param vars
     * @param index
     * @param others
     * @param initialMonomials
     * @param finalMonomials
     */
    private void replaceMonomialsByResult(String initialMonomial, String vars, int index, Map<BigDecimal, Integer> others,
                                          List<String> initialMonomials, List<String> finalMonomials) {
        BigDecimal finalTotal = new BigDecimal(ExpressionUtils.toNumericValue(initialMonomial));
        for (BigDecimal value : others.keySet()) {
            LOGGER.debug("Value : " + value);
            finalTotal = finalTotal.add(value);
            // The compatible part is set to null in the list
            initialMonomials.set(others.get(value), null);
        }
        // The part itself is also set to null in the list
        initialMonomials.set(index, null);

        String numericTotal = finalTotal.toString();
        if (numericTotal.equals("1") && !vars.isEmpty()) {
            finalMonomials.add(vars);
        } else if (numericTotal.equals("-1") && !vars.isEmpty()) {
            finalMonomials.add("-" + vars);
        } else if(!numericTotal.matches("0\\.0+") && !numericTotal.equals("0")){
            finalMonomials.add(MathUtils.cutSignificantZero(numericTotal + vars));
        }
    }

    @Override
    public String inFormat(String origin) {
        return FormatterFactory.removeFractions(origin);
    }

    @Override
    public String outFormat(String origin) {
        return ExpressionUtils.addMultShortcut(origin);
    }

    public static Sum getInstance() {
        return INSTANCE;
    }
}
