package net.akami.mask.handler;

import net.akami.mask.affection.MaskContext;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.FormatterFactory;
import net.akami.mask.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class PowCalculator extends BinaryOperationHandler {

    private static final PowCalculator INSTANCE = new PowCalculator();

    public PowCalculator() {
        this(MaskContext.DEFAULT);
    }

    public PowCalculator(MaskContext context) {
        super(context);
    }

    @Override
    public String operate(String a, String b) {
        LOGGER.info("PowCalculator operation process between {} and {} : \n", a, b);

        String aVars = ExpressionUtils.toVariables(a);
        String bVars = ExpressionUtils.toVariables(b);

        LOGGER.debug("aVars : {}, bVars : {}", aVars, bVars);
        if (aVars.length() == 0 && bVars.length() == 0) {
            String result = String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
            LOGGER.info("No variable found, return a^b value : {}", result);
            return result;
        }
        float powValue;
        // If pow value is too high, there is no point in developing the entire expression
        if (bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 199 ||
                (aVars.length() != 0 && powValue % 1 != 0)) {
            LOGGER.info("Pow value contains variables or pow value is greater than 199. Returns a^b");
            a = ExpressionUtils.isReduced(a) ? a : "(" + a + ")";
            b = ExpressionUtils.isReduced(b) ? b : "(" + b + ")";
            return a + "^" + b;
        }

        clearBuilder();
        StringBuilder builder = new StringBuilder();
        builder.append(a);
        for (int i = 1; i < powValue; i++) {
            builder.replace(0, builder.length(), Multiplicator.getInstance().rawOperate(builder.toString(), a));
            LOGGER.info("{} steps left. Currently : {}", powValue - i - 1, builder.toString());
        }
        return builder.toString();
    }

    // Alternative method not ready yet, to increase performances
    public String fastOperate(String a, String b) {
        LOGGER.info("PowCalculator operation process between {} and {} : \n", a, b);

        String aVars = ExpressionUtils.toVariables(a);
        String bVars = ExpressionUtils.toVariables(b);

        LOGGER.debug("aVars : {}, bVars : {}", aVars, bVars);
        if (aVars.length() == 0 && bVars.length() == 0) {
            String result = String.valueOf(Math.pow(Float.parseFloat(a), Float.parseFloat(b)));
            LOGGER.info("No variable found, return a^b value : {}", result);
            return result;
        }
        float powValue;
        // If pow value is too high, there is no point in developing the entire expression
        if (bVars.length() != 0 || (powValue = Float.parseFloat(b)) > 199 ||
                (aVars.length() != 0 && powValue % 1 != 0)) {
            LOGGER.info("Pow value contains variables or pow value is greater than 199. Returns a^b");
            a = ExpressionUtils.isReduced(a) ? a : "(" + a + ")";
            b = ExpressionUtils.isReduced(b) ? b : "(" + b + ")";
            return a + "^" + b;
        }
        List<String> results = new ArrayList<>();
        for(int i = 0; i<powValue; i++)
            results.add(a);

        int index = results.size();

        while(index != 1) {
            if(index%2 != 0) {
                index--;
                continue;
            }
            List<String> temp = new ArrayList<>();
            System.out.println("Mult between "+results.get(0)+" and "+results.get(1));
            String localResult = MathUtils.sum(Multiplicator.getInstance().rawOperate(results.get(0), results.get(1)), "");
            for(int i = 0; i < index/2; i++)
                temp.add(localResult);

            for(int i = 0; i < index; i++) {
                results.remove(0);
            }
            temp.addAll(results);
            results = temp;
            index /= 2;
        }
        String finalResult = results.get(0);
        for(String part : results) {
            if(part.equals(finalResult))
                continue;
            System.out.println("Mult between "+finalResult+" and "+part);
            finalResult = MathUtils.sum(Multiplicator.getInstance().rawOperate(finalResult, part), "");
        }
        return finalResult;
    }

    @Override
    public String inFormat(String origin) {
        return FormatterFactory.removeFractions(origin);
    }

    @Override
    public String outFormat(String origin) {
        return MathUtils.cutSignificantZero(FormatterFactory.removeMultiplicationSigns(origin));
    }

    public static PowCalculator getInstance() {
        return INSTANCE;
    }
}
