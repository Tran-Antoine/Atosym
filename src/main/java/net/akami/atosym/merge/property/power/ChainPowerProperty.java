package net.akami.atosym.merge.property.power;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.PowerMathObject;
import net.akami.atosym.merge.property.FairOverallMergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChainPowerProperty extends FairOverallMergeProperty<MathObject> {

    private PowerMathObject power1;
    private PowerMathObject power2;

    public ChainPowerProperty(MathObject p1, MathObject p2) {
        super(p1, p2, false);
    }

    @Override
    protected MathObject computeResult() {
        List<MathObject> elements = new ArrayList<>();
        addToList(p1, power1, elements);
        addToList(p2, power2, elements);

        if(elements.size() == 1) {
            return elements.get(0);
        }
        return new PowerMathObject(elements);
    }

    private void addToList(MathObject object, PowerMathObject powerVersion, List<MathObject> target) {
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

    private boolean prepare(MathObject object, Consumer<PowerMathObject> setter) {
        if(object.getType() == MathObjectType.POW) {
            setter.accept((PowerMathObject) object);
            return true;
        }
        return false;
    }

    private void setPower1(PowerMathObject power1) {
        this.power1 = power1;
    }

    private void setPower2(PowerMathObject power2) {
        this.power2 = power2;
    }
}
