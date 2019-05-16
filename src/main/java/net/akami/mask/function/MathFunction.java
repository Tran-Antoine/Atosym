package net.akami.mask.function;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.expression.ExpressionEncapsulator;
import net.akami.mask.handler.CancellableHandler;
import net.akami.mask.handler.PostCalculationActionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class MathFunction implements CancellableHandler, PostCalculationActionable, ExpressionEncapsulator {

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
    protected final String name;

    public MathFunction(char binding, String name) {
        this.binding = binding;
        this.name = name;
        addToFunctions();
    }

    protected abstract String operate(String... input);

    public String rawOperate(String... input) {
        if(isCancellable(input)) {
            return null;
            // TODO return findResult(input);
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

    public static Optional<MathFunction> getByExpression(String self) {
        for(char c : self.toCharArray()) {
            Optional<MathFunction> function = getByBinding(c);
            if(function.isPresent())
                return function;
        }
        return Optional.empty();
    }

    // TODO do something working for all functions.
    @Override
    public void postCalculation(Object result, Object... input) {
        // TODO
        //String calculation = input[0].equals(String.valueOf(this.binding)) ? input[1] : input[0];
        //getAffection(CalculationCache.class).get().push(calculation, result);
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MathFunction))
            return false;

        return this.binding == ((MathFunction) obj).binding;
    }

    @Override
    public String[] getEncapsulationString() {
        String[] parts = new String[2];
        parts[0] = name + '(';
        parts[1] = ")";
        return parts;
    }
}
