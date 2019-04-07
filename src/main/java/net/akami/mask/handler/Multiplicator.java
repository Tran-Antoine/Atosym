package net.akami.mask.handler;

import net.akami.mask.operation.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class Multiplicator extends BinaryOperation {

    private static final Multiplicator INSTANCE = new Multiplicator();
    private static final MathContext CONTEXT = new MathContext(120);

    public Multiplicator() {
        this(MaskContext.DEFAULT);
    }

    public Multiplicator(MaskContext context) {
        super(context);
    }

    @Override
    protected String operate(String a, String b) {

        LOGGER.error("Operating mult {} * {}", a, b);
        // Means we want to calculate the sin/cos/tan value of 'a'
        if(ExpressionUtils.isTrigonometricShortcut(b)) {
            LOGGER.info("Trigonometry calculation with {} and {}", a, b);
            String result = trigonometryOperation(a.isEmpty() ? "0" : a, b);
            LOGGER.info("Trigonometry findResult : {}", result);
            return result;
        }
        LOGGER.info("Multiplicator process of {} |*| {}: \n", a, b);

        List<String> aMonomials = ExpressionUtils.toMonomials(a);
        List<String> bMonomials = ExpressionUtils.toMonomials(b);

        // We can't use the constant BUILDER, because it is cleared repeatedly inside the loop
        StringBuilder builder = new StringBuilder();

        for (String part : aMonomials) {
            for (String part2 : bMonomials) {
                LOGGER.debug("Treating simple mult : {} |*| {}", part, part2);
                String result = simpleMult(part, part2);
                LOGGER.error("Result of simple mult between {} and {} : {}", part, part2, result);
                boolean first = part.equals(aMonomials.get(0)) && part2.equals(bMonomials.get(0));
                if (result.startsWith("+") || result.startsWith("-") || first) {
                    builder.append(result);
                } else {
                    builder.append("+" + result);
                }
            }
        }
        String unReducedResult = builder.toString();
        LOGGER.info("FINAL RESULT : {}", unReducedResult);
        String finalResult = Adder.getInstance().operate(unReducedResult, "");
        LOGGER.error("- Result of mult {} |*| {} : {}", a, b, finalResult);
        return finalResult;
    }

    /**
     * Calculates a*b. both strings must not be polynomials. If you don't know whether a and b are monomials,
     * call {@link MathUtils#mult(String, String)} instead.
     *
     * @param a the first value
     * @param b the second value
     * @return the findResult of the multiplication between a and b
     * @throws IllegalArgumentException if a and b are not monomials
     */
    public String simpleMult(String a, String b) {

        String concatenated = a + "*" + b;
        String originalVars = ExpressionUtils.toVariables(concatenated);
        LOGGER.debug("Variables of {} and {} : {}", a, b, originalVars);
        LOGGER.error("Numeric value of {} and {}", a, b);
        a = ExpressionUtils.toNumericValue(a);
        b = ExpressionUtils.toNumericValue(b);
        LOGGER.error("are {} and {}", a, b);
        BigDecimal aValue = new BigDecimal(a);
        BigDecimal bValue = new BigDecimal(b);
        String floatResult = MathUtils.cutSignificantZero(aValue.multiply(bValue, CONTEXT).toString());
        if (MathUtils.roundPeriodicSeries(floatResult).equals("1")) {
            if (!originalVars.isEmpty()) {
                return originalVars;
            }
        }
        LOGGER.debug("Float findResult : {}", floatResult);
        return floatResult + originalVars;
    }

    private static String trigonometryOperation(String a, String b) {
        switch (b) {
            case "@":
                return MathUtils.sin(a);
            case "#":
                return MathUtils.cos(a);
            default:
                return MathUtils.tan(a);
        }
    }

    @Override
    public String inFormat(String origin) {
        String result = FormatterFactory.removeFractions(origin);
        LOGGER.debug("{} became {}", origin, result);
        return result;
    }

    @Override
    public String outFormat(String origin) {
        return FormatterFactory.removeMultiplicationSigns(origin);
    }

    public static Multiplicator getInstance() {
        return INSTANCE;
    }
}
