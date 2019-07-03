package net.akami.mask.handler;

import net.akami.mask.alteration.CalculationCache;
import net.akami.mask.alteration.CalculationCanceller;
import net.akami.mask.alteration.IOCalculationModifier;
import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class BinaryOperationHandler implements AlterationHandler<Expression, Expression>, PostCalculationActionable<Expression> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BinaryOperationHandler.class);
    protected MaskContext context;
    private List<CalculationCanceller<Expression>> cancellers;
    private List<IOCalculationModifier<Expression>> modifiers;

    public BinaryOperationHandler(MaskContext context) {
        this.context = context;
        this.cancellers = new ArrayList<>(Arrays.asList(
                new CalculationCache()
        ));
        this.modifiers = new ArrayList<>();
    }

    protected abstract Expression operate(Expression a, Expression b);

    public Expression rawOperate(Expression a, Expression b) {

        Expression[] rawExpressions = {a, b};
        for(IOCalculationModifier<Expression> modifier : getSuitableModifiers(rawExpressions)) {
            rawExpressions = modifier.modify(rawExpressions);
        }

        Optional<CalculationCanceller<Expression>> canceller = getSuitableCanceller(rawExpressions);
        if(canceller.isPresent()) return canceller.get().resultIfCancelled(rawExpressions);


        Expression result = operate(rawExpressions[0], rawExpressions[1]);
        postCalculation(result, rawExpressions);
        return result;
    }

    @Override
    public void postCalculation(Expression result, Expression... input) {
        String toPush = input[0] + "|" + input[1];
        getCanceller(CalculationCache.class).ifPresent(alteration -> alteration.push(toPush, result));
    }

    public static Set<BinaryOperationHandler> generateDefaultHandlers(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new Adder(context),
                new Subtractor(context),
                new Multiplier(context),
                new Divider(context),
                new PowerCalculator(context)
        ));
    }

    @Override
    public List<CalculationCanceller<Expression>> getCancellers() {
        return cancellers;
    }

    @Override
    public List<IOCalculationModifier<Expression>> getModifiers() {
        return modifiers;
    }

    public void addCanceller(CalculationCanceller<Expression> canceller) {
        cancellers.add(canceller);
    }

    public void removeCanceller(CalculationCanceller<Expression> canceller) {
        cancellers.remove(canceller);
    }

    public void setCancellers(List<CalculationCanceller<Expression>> cancellers) {
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
