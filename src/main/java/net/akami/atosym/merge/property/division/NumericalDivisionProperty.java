package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.merge.property.BiElementMergeProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class NumericalDivisionProperty extends BiElementMergeProperty<MathObject> {

    private MaskContext context;

    public NumericalDivisionProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> listA, List<MathObject> listB) {
        if(NumericUtils.isAnInteger(p1) && NumericUtils.isAnInteger(p2)) {
            int[] result = simplifyFraction(NumericUtils.asInt(p1), NumericUtils.asInt(p2));
            listA.add(new NumberExpression((float) result[0]));
            listB.add(new NumberExpression((float) result[1]));
            return;
        }

        float divisionResult = NumericUtils.div(NumericUtils.asFloat(p1), NumericUtils.asFloat(p2), context);
        listA.add(new NumberExpression(divisionResult));
    }

    @Override
    public boolean prepare() {
        return p1.getType() == MathObjectType.NUMBER && p2.getType() == MathObjectType.NUMBER;
    }

    private int[] simplifyFraction(int num, int den) {
        int divisor = gcd(num, den);
        return new int[]{num/divisor, den/divisor};
    }

    private int gcd(int a, int b) {
        int divisor = Math.min(a, b);
        for(;divisor > 0; divisor--) {
            if(a % divisor == 0 && b % divisor == 0) {
                return divisor;
            }
        }
        // useless statement, the previous if statement always end up being true
        return 1;
    }
}
