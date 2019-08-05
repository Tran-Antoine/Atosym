package net.akami.atosym.function;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.FairAlterationHandler;
import net.akami.atosym.alteration.IOCalculationModifier;
import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.Monomial;
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
public abstract class MathFunction implements
        FairAlterationHandler<Expression>, PostCalculationActionable<Expression>, CompleteCoverOverlay {

    protected List<CalculationCanceller<Expression, Expression>> cancellers;
    protected List<IOCalculationModifier<Expression>> modifiers;
    protected final char binding;
    protected final String name;
    private final MaskContext context;
    private final int argsLength;

    public MathFunction(char binding, String name, MaskContext context, int argsLength) {
        this.binding = binding;
        this.name = name;
        this.context = context;
        this.argsLength = argsLength;
        initAlterations();
    }

    protected abstract Expression operate(Expression... input);

    public Expression rawOperate(Expression... input) {
        if(input.length != argsLength) throw new IllegalArgumentException
                (input.length+" params given, only "+argsLength+" required.");
        for(IOCalculationModifier<Expression> modifier : getSuitableModifiers(input)) {
            input = modifier.modify(input);
        }

        Optional<CalculationCanceller<Expression, Expression>> canceller = getSuitableCanceller(input);
        if(canceller.isPresent()) return canceller.get().resultIfCancelled(input);
        return operate(input);
    }

    public boolean exists() {
        return context.getFunctionByBinding(this.binding).isPresent();
    }

    @Override
    public void postCalculation(Expression result, Expression... input) {
        //String calculation = input[0].equals(String.valueOf(this.binding)) ? input[1] : input[0];
        //getalteration(CalculationCache.class).getElement().push(calculation, merge);
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
        if(!(obj instanceof MathFunction))
            return false;

        return this.binding == ((MathFunction) obj).binding;
    }

    @Override
    public String[] getEncapsulationString(List<Monomial> elements, int index, List<ExpressionOverlay> others) {
        String[] parts = new String[2];
        parts[0] = name + '(';
        parts[1] = ")";
        return parts;
    }

    public char getBinding() {
        return binding;
    }

    public String getName() {
        return name;
    }

    private void initAlterations() {
        this.cancellers = new ArrayList<>();
        this.modifiers = new ArrayList<>();
    }

    public static Set<MathFunction> generateDefaultFunctions(MaskContext context) {
        return new HashSet<>(Arrays.asList(
                new SineFunction(context),
                new CosineFunction(context),
                new TangentFunction(context),
                new SquareRootFunction(context),
                new DegreesToRadiansFunction(context),
                new PiValue(context)
        ));
    }

    public void removeCanceller(CalculationCanceller<Expression, Expression> canceller) {
        cancellers.remove(canceller);
    }

    public void setCancellers(List<CalculationCanceller<Expression, Expression>> cancellers) {
        this.cancellers = cancellers;
    }

    public void removeModifier(IOCalculationModifier<Expression> modifier) {
        modifiers.remove(modifier);
    }

    public void setModifiers(List<IOCalculationModifier<Expression>> modifiers) {
        this.modifiers = modifiers;
    }
}
