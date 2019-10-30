package net.akami.atosym.merge.property.power;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChainPowerProperty extends FairOverallMergeProperty<MathObject> {

    private ExponentMathObject power1;
    private ExponentMathObject power2;

    private MaskContext context;

    public ChainPowerProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2);
        this.context = context;
    }

    @Override
    protected MathObject computeResult() {
        List<MathObject> elements = new ArrayList<>();
        addToList(p1, power1, elements);
        addToList(p2, power2, elements);

        if(elements.size() == 1) {
            return elements.get(0);
        }
        return new ExponentMathObject(elements, context);
    }

    private void addToList(MathObject object, ExponentMathObject powerVersion, List<MathObject> target) {
        if(powerVersion == null) {
            target.add(object);
        } else {
            target.addAll(powerVersion.getChildren());
        }
    }

    @Override
    public boolean prepare() {
        return prepare(p1, this::setPower1) | prepare(p2, this::setPower2);
    }

    private boolean prepare(MathObject object, Consumer<ExponentMathObject> setter) {
        if(object.getType() == MathObjectType.POW) {
            setter.accept((ExponentMathObject) object);
            return true;
        }
        return false;
    }

    private void setPower1(ExponentMathObject power1) {
        this.power1 = power1;
    }

    private void setPower2(ExponentMathObject power2) {
        this.power2 = power2;
    }
}
