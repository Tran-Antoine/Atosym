package net.akami.atosym.operator;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.FairAlterationHandler;
import net.akami.atosym.alteration.IOCalculationModifier;
import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.handler.PostCalculationActionable;


import java.util.*;

/**
 * Represents a mathematical operator matching the following syntax : {@code name(p1, p2, ...)}.
 * <br><br>
 * The currently available functions are the trigonometry functions exclusively. More functions with multiple
 * parameters, such as {@code log} or {@code root} will be added in the future.
 * @author Antoine Tran
 */
public abstract class MathOperator implements
        FairAlterationHandler<MathObject>, PostCalculationActionable<MathObject> {

    protected List<CalculationCanceller<MathObject, MathObject>> cancellers;
    protected List<IOCalculationModifier<MathObject>> modifiers;
    protected final List<String> names;
    private final int argsLength;

    protected MaskContext context;

    public MathOperator(List<String> names, int argsLength, MaskContext context) {
        this.names = names;
        this.argsLength = argsLength;
        this.context = context;
        initAlterations();
    }

    protected abstract MathObject operate(List<MathObject> input);

    public MathObject rawOperate(List<MathObject> input) {
        checkInputSize(input.size());
        for(IOCalculationModifier<MathObject> modifier : getSuitableModifiers(input)) {
            input = modifier.modify(input);
        }

        Optional<CalculationCanceller<MathObject, MathObject>> canceller = getSuitableCanceller(input);
        if(canceller.isPresent()) {
            return canceller.get().resultIfCancelled(input);
        }

        return operate(input);
    }

    protected void checkInputSize(int size) {
        if(size != argsLength)
            throw new IllegalArgumentException(size + " params given " + argsLength + " required.");
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

        return this.names.equals(((MathOperator) obj).names);
    }

    public List<String> getNames() {
        return names;
    }

    public boolean matches(String name) {
        return names.contains(name);
    }

    private void initAlterations() {
        this.cancellers = new ArrayList<>();
        this.modifiers = new ArrayList<>();
    }

    public static Set<MathOperator> generateDefaultOperators(MaskContext context) {
        Set<MathOperator> operators = new HashSet<>(Arrays.asList(
                new SineOperator(context),
                new CosineOperator(context),
                new TangentOperator(context),
                new PriorityOperator(context),
                new RootOperator(context),
                new FactorialOperator(context)
        ));

        operators.addAll(BinaryOperator.generateDefaultBinaryOperators(context));
        return operators;
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

    public int getSize() {
        return argsLength;
    }
}
