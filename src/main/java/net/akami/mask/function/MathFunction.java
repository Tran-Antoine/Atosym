package net.akami.mask.function;

import net.akami.mask.affection.CalculationCanceller;
import net.akami.mask.core.MaskContext;
import net.akami.mask.encapsulator.CompleteCoverEncapsulator;
import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.expression.ExpressionElement;
import net.akami.mask.handler.CancellableHandler;
import net.akami.mask.handler.PostCalculationActionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Represents a mathematical function matching the following syntax : {@code name(p1, p2, ...)}.
 * <pre></pre>
 * The currently available functions are the trigonometry functions exclusively. More functions with multiple
 * parameters, such as {@code log} or {@code root} will be added in the future.
 * @author Antoine Tran
 */
public abstract class MathFunction implements CancellableHandler<String>, PostCalculationActionable, CompleteCoverEncapsulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathFunction.class);

    protected final List<CalculationCanceller<String>> cancellers = new ArrayList<>();
    protected final char binding;
    protected final String name;
    private final MaskContext context;

    public MathFunction(char binding, String name, MaskContext context) {
        this.binding = binding;
        this.name = name;
        this.context = context;
    }

    protected abstract String operate(String... input);

    public String rawOperate(String... input) {
        if(isCancellable(input)) {
            return null;
            // TODO return findResult(input);
        }
        return operate(input);
    }

    public boolean exists() {
        return context.getFunctionByBinding(this.binding).isPresent();
    }

    // TODO do something working for all functions.
    @Override
    public void postCalculation(Object result, Object... input) {
        // TODO complete
        //String calculation = input[0].equals(String.valueOf(this.binding)) ? input[1] : input[0];
        //getAffection(CalculationCache.class).get().push(calculation, merge);
    }

    @Override
    public List<CalculationCanceller<String>> getAffections() {
        return cancellers;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MathFunction))
            return false;

        return this.binding == ((MathFunction) obj).binding;
    }

    @Override
    public String[] getEncapsulationString(List<ExpressionElement> elements, int index, List<ExpressionEncapsulator> others) {
        String[] parts = new String[2];
        parts[0] = name + '(';
        parts[1] = ")";
        return parts;
    }

    public char getBinding() {
        return binding;
    }

    public static Set<MathFunction> generateDefaultFunctions(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new SinusFunction(context),
                new CosineFunction(context),
                new TangentFunction(context)
        ));
    }
}
