package net.akami.mask.function;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.handler.CancellableHandler;
import net.akami.mask.handler.PostCalculationActionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class MathFunction implements CancellableHandler, PostCalculationActionable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathFunction.class);
    private static final List<MathFunction> functions = new ArrayList<>();

    static {
        functions.addAll(Arrays.asList(
                new SinusFunction(),
                new CosineFunction(),
                new TangentFunction()));
    }

    private final CalculationCanceller[] cancellers = {new CalculationCache()};
    protected final char binding;

    public MathFunction(char binding) {
        this.binding = binding;
        addToFunctions();
    }

    protected abstract String operate(String... input);

    public String rawOperate(String... input) {
        if(isCancellable(input)) {
            return findResult(input);
        }
        return operate(input);
    }

    private void addToFunctions() {
        getByBinding(this.binding).ifPresent(e -> {
            functions.remove(e);
            LOGGER.warn("New function added, although another existing function with the same binding has been found.");
        });
        functions.add(this);
    }

    public boolean exists() {
        return getByBinding(this.binding).isPresent();
    }

    public static Optional<MathFunction> getByBinding(char binding) {
        for(MathFunction function : functions) {
            if(function.binding == binding)
                return Optional.of(function);
        }
        return Optional.empty();
    }

    // TODO do something working for all functions.
    @Override
    public void postCalculation(String result, String... input) {
        String calculation = input[0].equals(String.valueOf(this.binding)) ? input[1] : input[0];
        getAffection(CalculationCache.class).get().push(calculation, result);
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }
}
