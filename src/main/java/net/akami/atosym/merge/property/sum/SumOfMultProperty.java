package net.akami.atosym.merge.property.sum;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.merge.property.FairElementMergeProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SumOfMultProperty extends FairElementMergeProperty<MathObject> {

    private MultMathObject m1;
    private MultMathObject m2;

    private List<MathObject> variableElements;
    private NumberExpression number1;
    private NumberExpression number2;

    private MaskContext context;

    public SumOfMultProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        NumberExpression result;
        if(number1 == null && number2 == null) {
            result = new NumberExpression(2f);
        } else if(number1 == null) {
            result = new NumberExpression(number2, (NumberExpression) MathObject.NEUTRAL_MULT, NumericUtils::sum, context);
        } else if(number2 == null){
            result = new NumberExpression(number1, (NumberExpression) MathObject.NEUTRAL_MULT, NumericUtils::sum, context);
        } else {
            result = new NumberExpression(number1, number2, NumericUtils::sum, context);
        }

        variableElements.add(result);
        constructed.add(new MultMathObject(variableElements, context));
    }

    @Override
    public boolean prepare() {
        if (! (initMultObjects(p1, this::setM1) & initMultObjects(p2, this::setM2))) {
            return false;
        }

        return areObjectsCompatible();
    }

    private boolean initMultObjects(MathObject object, Consumer<MultMathObject> setter) {
        if(object.getType() == MathObjectType.MULT) {
            setter.accept((MultMathObject) object);
            return true;
        }

        if(object.getType() == MathObjectType.VARIABLE) {
            List<MathObject> transformedVariable = Arrays.asList(new NumberExpression(1f), object);
            setter.accept(new MultMathObject(transformedVariable, context));
            return true;
        }

        return false;
    }

    private boolean areObjectsCompatible() {
        List<MathObject> variables1 = createNumberlessList(m1, this::setNumber1);
        List<MathObject> variables2 = createNumberlessList(m2, this::setNumber2);
        if(variables1.equals(variables2)) {
            this.variableElements = variables1;
            return true;
        }
        return false;
    }

    private List<MathObject> createNumberlessList(MultMathObject target, Consumer<NumberExpression> setter) {
        List<MathObject> variableElements1 = new ArrayList<>();
        target.addChildrenTo(variableElements1);
        return variableElements1
                .stream()
                .filter(object -> filterNumbers(object, setter))
                .collect(Collectors.toList());

    }

    private boolean filterNumbers(MathObject current, Consumer<NumberExpression> setter) {
        if(current.getType() == MathObjectType.NUMBER) {
            setter.accept((NumberExpression) current);
            return false;
        }
        return true;
    }
    // Used for method referencing
    private void setM1(MultMathObject m1) {
        this.m1 = m1;
    }

    // Used for method referencing
    private void setM2(MultMathObject m2) {
        this.m2 = m2;
    }

    private void setNumber1(NumberExpression number1) {
        this.number1 = number1;
    }

    private void setNumber2(NumberExpression number2) {
        this.number2 = number2;
    }
}
