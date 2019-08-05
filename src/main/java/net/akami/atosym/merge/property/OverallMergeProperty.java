package net.akami.atosym.merge.property;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.IOCalculationModifier;
import net.akami.atosym.handler.AlterationHandler;
import net.akami.atosym.merge.MergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Properties which have an appliance depending on both entire sequences. If a property is detected as
 * suitable for the two lists, it will be able to compute a full result from the two lists, and potentially require
 * a restart merge.
 * @param <P> what type of object is handled by the property
 */
public abstract class OverallMergeProperty<P, R> extends MergeProperty<P> implements AlterationHandler<P, R> {

    protected List<IOCalculationModifier<P>> modifiers = new ArrayList<>();
    protected List<CalculationCanceller<P, R>> cancellers = new ArrayList<>();

    public OverallMergeProperty(P p1, P p2, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
    }

    protected abstract R computeResult();

    public R rawComputeResult() {
        // secure cast. Java does not support generic arrays creation
        P[] rawExpressions = (P[]) new Object[]{p1, p2};
        for(IOCalculationModifier<P> modifier : getSuitableModifiers(rawExpressions)) {
            rawExpressions = modifier.modify(rawExpressions);
        }

        Optional<CalculationCanceller<P, R>> canceller = getSuitableCanceller(rawExpressions);
        if(canceller.isPresent()) return canceller.get().resultIfCancelled(rawExpressions);

        this.p1 = rawExpressions[0];
        this.p2 = rawExpressions[1];

        return computeResult();
    }

    @Override
    public List<IOCalculationModifier<P>> getModifiers() {
        return modifiers;
    }

    @Override
    public List<CalculationCanceller<P, R>> getCancellers() {
        return cancellers;
    }
}
