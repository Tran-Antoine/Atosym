package net.akami.mask.function;

import net.akami.mask.affection.CalculationCache;
import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.handler.CancellableHandler;
import net.akami.mask.handler.PostCalculationActionable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// TODO : Use String[] instead, some functions may have multiple arguments (log, root, exp)
public abstract class MathFunction implements CancellableHandler, PostCalculationActionable {

    private static final List<MathFunction> functions = new ArrayList<>();

    static {
        functions.addAll(Arrays.asList(new CosinusFunction()));
    }

    private final CalculationCanceller[] cancellers = {new CalculationCache()};
    protected final char binding;

    public MathFunction(char binding) {
        this.binding = binding;
        addToFunctions();
    }

    protected abstract String operate(String input);

    public String rawOperate(String input) {
        if(isCancellable(input)) {
            return findResult(input);
        }
        return operate(input);
    }

    private void addToFunctions() {
        getByBinding(this.binding).ifPresent(e -> functions.remove(e));
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

    @Override
    public void postCalculation(String a, String b, String result) {
        String calculation = a.equals(String.valueOf(this.binding)) ? b : a;
        getAffection(CalculationCache.class).get().push(calculation, result);
    }

    @Override
    public CalculationCanceller[] getAffections() {
        return cancellers;
    }
}
