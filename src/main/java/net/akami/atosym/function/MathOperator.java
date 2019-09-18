package net.akami.atosym.function;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.FairAlterationHandler;
import net.akami.atosym.alteration.IOCalculationModifier;
import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.handler.PostCalculationActionable;
import net.akami.atosym.overlay.CompleteCoverOverlay;
import net.akami.atosym.overlay.ExpressionOverlay;

import java.util.*;

/**
 * Represents a mathematical function matching the following syntax : {@code name(p1, p2, ...)}.
 * <br><br>
 * The currently available functions are the trigonometry functions exclusively. More functions with multiple
 * parameters, such as {@code log} or {@code root} will be added in the future.
 * @author Antoine Tran
 */
public abstract class MathOperator implements
        FairAlterationHandler<Expression>, PostCalculationActionable<Expression>, CompleteCoverOverlay {

    protected List<CalculationCanceller<Expression, Expression>> cancellers;
    protected List<IOCalculationModifier<Expression>> modifiers;
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

        for(IOCalculationModifier<Expression> modifier : getSuitableModifiers(input)) {
            input = modifier.modify(input);
        }

        Optional<CalculationCanceller<Expression, Expression>> canceller = getSuitableCanceller(input);
        if(canceller.isPresent()) {
            return canceller.get().resultIfCancelled(input);
        }

        return operate(input);
    }

    @Override
    public void postCalculation(Expression result, Expression... input) {

    }

    @Override
    public List<CalculationCanceller<Expression, Expression>> getCancellers() {
        return cancellers;
    }

    @Override
    public List<IOCalculationModifier<Expression>> getModifiers() {
        return modifiers;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MathOperator))
            return false;

        return this.name.equals(((MathOperator) obj).name);
    }

    @Override
    public String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others) {
        String[] parts = new String[2];
        parts[0] = name + '(';
        parts[1] = ")";
        return parts;
    }

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

    public void addCanceller(CalculationCanceller<Expression, Expression> canceller) {
        cancellers.add(canceller);
    }

    public void removeCanceller(CalculationCanceller<Expression, Expression> canceller) {
        cancellers.remove(canceller);
    }

    public void setCancellers(List<CalculationCanceller<Expression, Expression>> cancellers) {
        this.cancellers = cancellers;
    }
    public void addModifier(IOCalculationModifier<Expression> modifier) {
        modifiers.add(modifier);
    }

    public void removeModifier(IOCalculationModifier<Expression> modifier) {
        modifiers.remove(modifier);
    }

    public void setModifiers(List<IOCalculationModifier<Expression>> modifiers) {
        this.modifiers = modifiers;
    }
}
