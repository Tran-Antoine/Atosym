package net.akami.mask.structure;

import net.akami.mask.math.MaskExpression;
import net.akami.mask.math.MaskOperator;
import net.akami.mask.utils.ExpressionUtils;
import net.akami.mask.utils.MathUtils;

import java.util.*;

public class EquationCalculator {

    public static String solve(List<BiMask> biMasks) {

        int varIndex = 0;
        BiMask biMask = biMasks.get(varIndex);
        MaskExpression leftMask = biMask.left;
        MaskExpression rightMask = biMask.right;
        MaskOperator op = MaskOperator.begin();
        op.reduce(leftMask, leftMask).reduce(rightMask, rightMask);
        String leftExp = leftMask.getExpression();
        String rightExp = rightMask.getExpression();

        char[] variables = ExpressionUtils.toVariablesType(leftExp+rightExp).toCharArray();
        char actualVar = variables[0];

        List<String> leftMonomials = ExpressionUtils.toMonomials(leftExp);
        List<String> rightMonomials = ExpressionUtils.toMonomials(rightExp);

        for(String monomial : leftMonomials) {
            if(!monomial.contains(String.valueOf(actualVar))) {
                int index = leftMonomials.indexOf(monomial);
                if(monomial.startsWith("+"))
                    monomial = "-" + monomial.substring(1);
                else if(monomial.startsWith("-"))
                    monomial = "+" + monomial.substring(1);
                else
                    monomial = "-" + monomial;
                leftMonomials.set(index, null);
                rightMonomials.add(monomial);
            }
        }

        for(String monomial : rightMonomials) {
            if(monomial.contains(String.valueOf(actualVar))) {
                int index = rightMonomials.indexOf(monomial);
                if(monomial.startsWith("+"))
                    monomial = "-" + monomial.substring(1);
                else if(monomial.startsWith("-"))
                    monomial = "+" + monomial.substring(1);
                else
                    monomial = "-" + monomial;
                rightMonomials.set(index, null);
                leftMonomials.add(monomial);
            }
        }

        String numericLeftValue = ExpressionUtils.toNumericValue(MathUtils.sum(leftMonomials));
        return MathUtils.divide(MathUtils.sum(rightMonomials), numericLeftValue);
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
    }
}
