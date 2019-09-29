package net.akami.atosym.function;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.FairAlterationHandler;
import net.akami.atosym.alteration.IOCalculationModifier;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.handler.PostCalculationActionable;


import java.util.*;

/**
 * Represents a mathematical function matching the following syntax : {@code name(p1, p2, ...)}.
 * <br><br>
 * The currently available functions are the trigonometry functions exclusively. More functions with multiple
 * parameters, such as {@code log} or {@code root} will be added in the future.
 * @author Antoine Tran
 */
public abstract class MathOperator implements
        FairAlterationHandler<MathObject>, PostCalculationActionable<MathObject> {

    protected List<CalculationCanceller<MathObject, MathObject>> cancellers;
    protected List<IOCalculationModifier<MathObject>> modifiers;
    protected final String name;
    private final int argsLength;

    public MathOperator(String name, int argsLength) {
        this.name = name;
        this.argsLength = argsLength;
        initAlterations();
    }

    protected abstract MathObject operate(MathObject... input);

    public MathObject rawOperate(MathObject... input) {
        if(input.length != argsLength) {
            throw new IllegalArgumentException(input.length + " params given, only " + argsLength + " required.");
        }

        for(IOCalculationModifier<MathObject> modifier : getSuitableModifiers(input)) {
            input = modifier.modify(input);
        }

        Optional<CalculationCanceller<MathObject, MathObject>> canceller = getSuitableCanceller(input);
        if(canceller.isPresent()) {
            return canceller.get().resultIfCancelled(input);
        }

        return operate(input);
    }

    @Override
    public void postCalculation(MathObject result, MathObject... input) {

    }

    @Override
    public List<CalculationCanceller<MathObject, MathObject>> getCancellers() {
        return cancellers;
    }

    @Override
    public List<IOCalculationModifier<MathObject>> getModifiers() {
        return modifiers;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MathOperator))
            return false;

        return this.name.equals(((MathOperator) obj).name);
    }

    /*@Override
    public String[] getEncapsulationString(List<MathObject> elements, int index, List<ExpressionOverlay> others) {
        String[] parts = new String[2];
        parts[0] = name + '(';
        parts[1] = ")";
        return parts;
    }*/

    public String getName() {
        return name;
    }

    private void initAlterations() {
        this.cancellers = new ArrayList<>();
        this.modifiers = new ArrayList<>();
    }

    public static Set<MathOperator> generateDefaultFunctions() {
        return new HashSet<>(Arrays.asList(
                new SineOperator(),
                new CosineOperator(),
                new TangentOperator(),
                new RootOperator(),
                new DegreesToRadiansOperator()
        ));
    }

    public void addCanceller(CalculationCanceller<MathObject, MathObject> canceller) {
        cancellers.add(canceller);
    }

    public void removeCanceller(CalculationCanceller<MathObject, MathObject> canceller) {
        cancellers.remove(canceller);
    }

    public void setCancellers(List<CalculationCanceller<MathObject, MathObject>> cancellers) {
        this.cancellers = cancellers;
    }
    public void addModifier(IOCalculationModifier<MathObject> modifier) {
        modifiers.add(modifier);
    }

    public void removeModifier(IOCalculationModifier<MathObject> modifier) {
        modifiers.remove(modifier);
    }

    public void setModifiers(List<IOCalculationModifier<MathObject>> modifiers) {
        this.modifiers = modifiers;
    }
}
