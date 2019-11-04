package net.akami.atosym.sorting;

import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.utils.NumericUtils;

import java.util.function.Consumer;

public class ExponentLevelSorting implements SortingRule {

    private float maxPower1;
    private float totalPower1;

    private float maxPower2;
    private float totalPower2;

    @Override
    public boolean isRuleSuitable(MathObject o1, MathObject o2, MathObjectType parentType) {
        return parentType == MathObjectType.SUM || parentType == MathObjectType.SUB;
    }

    @Override
    public int compare(MathObject o1, MathObject o2, MathObjectType parentType) {
        setPower(o1, this::addPower1);
        setPower(o2, this::addPower2);
        int comparison = Float.compare(maxPower2, maxPower1);
        if(comparison == 0) {
            comparison = Float.compare(totalPower2, totalPower1);
        }
        maxPower1 = 0;
        maxPower2 = 0;
        totalPower1 = 0;
        totalPower2 = 0;
        return comparison;
    }

    // TODO implement a toPower method in MathObject ?
    private void setPower(MathObject o1, Consumer<Float> adder) {
        switch (o1.getType()) {
            case NUMBER:
                adder.accept(0f);
                return;
            case VARIABLE:
                adder.accept(1f);
                return;
            case MULT:
                for(MathObject o : ((FunctionObject) o1).getChildren()) {
                    setPower(o, adder);
                }
                return;
            case DIV:
                setPower(((FunctionObject) o1).getChild(0), adder);
                return;
            case POW:
                MathObject exponent = ((FunctionObject) o1).getChild(1);
                if(NumericUtils.isANumber(exponent)) {
                    adder.accept(NumericUtils.asFloat(exponent));
                } else {
                    throw new UnsupportedOperationException("Non numeric exponents cannot be sorted for now");
                }

        }
    }

    private void addPower1(float a) {
        if(maxPower1 < a) this.maxPower1 = a;
        this.totalPower1 += a;
    }

    private void addPower2(float a) {
        if(maxPower2 < a) this.maxPower2 = a;
        this.totalPower2 += a;
    }
}
