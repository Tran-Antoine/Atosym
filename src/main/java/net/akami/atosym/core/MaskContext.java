package net.akami.atosym.core;

import net.akami.atosym.alteration.*;
import net.akami.atosym.check.*;
import net.akami.atosym.exception.MaskException;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.comparison.SortingManager;
import net.akami.atosym.operator.MathOperator;
import net.akami.atosym.handler.AlterationHandler;
import net.akami.atosym.operator.BinaryOperator;
import net.akami.atosym.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines a calculation environment, required to perform any operation on expressions. <br>
 * A context (or environment) allows you to customize different calculations. <br>
 * You are free to addBranch some support for mathematical patterns that are not supported by the library, or, on the other hand,
 * remove some undesired features provided by default, which will make your calculations run faster. <br>
 * Furthermore, the environment gives you a total control over inputs and outputs, which means that a single expression
 * can merge in different outcomes. You will for instance be able to decide whether {@code 5/2} will remain {@code 5/2} or
 * will be reduced as {@code 2.5}. <br>
 * For further information about inputs and outputs alteration, see {@link CalculationAlteration} and
 * {@link AlterationHandler}. <br>
 *
 * The MaskContext class handles :
 * <ul>
 *      <li> A set of {@link BinaryOperator}s. Basically, the 5 default operations, being the addition, the
 *      subtraction, the multiplication, the division and the power calculation. Although this is not recommended,
 *      you are free to remove any of these operations, if you are guaranteed that they won't be required. However,
 *      because your expression doesn't literally contain a given operation doesn't mean it is not required. For
 *      instance, the power calculator requires the multiplier to work, since it basically chains multiplications a given
 *      amount of times. <br>
 *      Note : binary operations take care of the alteration system. You can directly modify the handlers, or addBranch your owns
 *      with different alterations. <br>
 *      <li> A set of {@link MathOperator}s. Only the mathematical functions present in the set wil be supported.
 *      Mathematical functions can require multiple arguments. See {@link MathOperator}'s documentation for further
 *      information <br>
 *      <li> A list of {@link ValidityCheck}. They analyze the given input and throw an error if the input is invalid
 *      mathematically speaking. <br>
 *      <li> A {@link MathContext}, used to define the amount of significant digits for calculations.
 *  </ul>
 *
 * 
 * @author Antoine Tran
 */
public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();

    private Set<MathOperator> supportedOperators;
    private List<ValidityCheck> validityChecks;
    private SortingManager sortingManager;
    private MathContext bigDecimalContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskContext.class);

    static {
        DEFAULT.addDuplicatedCanceller(CalculationCache::new, BinaryOperator.class);
    }
    /**
     * Constructs a context with the default parameters. The set of {@link BinaryOperator}s will be generated
     * from {@link BinaryOperator#generateDefaultBinaryOperators(MaskContext)}, the set of mathematical functions
     * will be generated from {@link MathOperator#generateDefaultOperators(MaskContext)}, and the MathContext will
     * use a 100 digits precision.
     */
    public MaskContext() {
        this(100);
    }

    /**
     * Constructs a context with the given precision. <br>
     * See {@link MaskContext#MaskContext()} for further information concerning the other handled data.
     * @param precision the amount of significant digits handled by the context
     */
    public MaskContext(int precision) {
        this.supportedOperators = MathOperator.generateDefaultOperators(this);
        this.bigDecimalContext = new MathContext(precision, RoundingMode.CEILING);
        this.validityChecks = defaultValidityChecks();
        this.sortingManager = SortingManager.DEFAULT;
    }

    /**
     * Performs a calculation from the two elements given and the selected operator. <br>
     * By default (recommended), the 5 operations (+-{@literal *}/^) are available. This method is generally called
     * from the {@link MathUtils} class
     * @param a the first element to compute with the second one
     * @param b the second element to compute with the first one
     * @param clazz the type of the binary operator in charge of the calculation
     * @return a result computed by the operator from the two elements given
     */
    public MathObject binaryCompute(MathObject a, MathObject b, Class<? extends BinaryOperator> clazz) {
        BinaryOperator handler = getBinaryOperator(clazz);
        return handler.rawOperate(Arrays.asList(a, b));
    }

    /**
     * Adds a new operator to the set. <br>
     * Note that the new operator will replace any existing operator whose binding would match with the new operator's.
     * @param target a new operator to addBranch
     */
    public void addOperator(MathOperator target) {
        supportedOperators.add(target);
    }

    /**
     * Removes the operator from the set whose {@link #getClass()} method equals the clazz parameter
     * @param clazz the class type to remove
     */
    public void removeOperator(Class<? extends MathOperator> clazz) {
        for(MathOperator func : supportedOperators) {
            if(func.getClass().equals(clazz)) {
                supportedOperators.remove(func);
                return;
            }
        }
        LOGGER.warn("Unable to remove operator from the given class name : {}", clazz.getName());
    }

    /**
     * @return the {@link MathContext} instance, used to define the calculation precision
     */
    public MathContext getMathContext() {
        return bigDecimalContext;
    }

    private List<ValidityCheck> defaultValidityChecks() {
        return Arrays.asList(
                new EmptyStringGiven(),
                new IllegalStartingCharacter(),
                new UnknownCharacter(),
                new BracketsCounterCheck(),
                new IllegalEndingCharacter()
        );
    }

    /**
     * Throws an error if the given Mask is invalid, according to the {@link ValidityCheck}s handled.
     * @param self the atosym to analyze
     */
    public void assertExpressionValidity(Mask self) {
        for(ValidityCheck check : validityChecks) {
            if(!check.isValid(self.getExpression()))
                throw new MaskException(check.errorMessage(self.getExpression()), self);
        }
    }

    /**
     * @return the list of math functions supported
     */
    public Set<MathOperator> getSupportedOperators() {
        return supportedOperators;
    }

    public void addGlobalModifier(IOCalculationModifier<MathObject> modifier, Class<?> type) {
        addClonedModifier(() -> modifier, type);
    }

    public void addGlobalCanceller(FairCalculationCanceller<MathObject> canceller, Class<?> type) {
        addDuplicatedCanceller(() -> canceller, type);
    }

    public void addClonedModifier(Supplier<IOCalculationModifier<MathObject>> modifier, Class<?> type) {
        actionGlobal(AlterationHandler::addModifier, modifier, type);
    }

    public void addDuplicatedCanceller(Supplier<FairCalculationCanceller<MathObject>> canceller, Class<?> type) {
        actionGlobal(AlterationHandler::addCanceller, canceller, type);
    }

    public void removeGlobalModifier(IOCalculationModifier<MathObject> modifier, Class<?> type) {
        actionGlobal(AlterationHandler::removeModifier, () -> modifier, type);
    }

    public void removeGlobalCanceller(FairCalculationCanceller<MathObject> canceller, Class<?> type) {
        actionGlobal(AlterationHandler::removeCanceller, () -> canceller, type);
    }

    private <S> void actionGlobal(AlterationAction<FairAlterationHandler<MathObject>, S> action, Supplier<S> s1, Class<?> type) {
        List<FairAlterationHandler<MathObject>> alterationHandlers = new ArrayList<>(supportedOperators);
        for(FairAlterationHandler<MathObject> element : alterationHandlers) {
            if(type.isAssignableFrom(element.getClass())) {
                action.action(element, s1.get());
            }
        }
    }

    public Optional<MathOperator> getOperator(String text) {
        for(MathOperator operator : supportedOperators) {
            if(operator.getNames().contains(text))
                return Optional.of(operator);
        }
        return Optional.empty();
    }

    /**
     * Returns the instance of a required operator, allowing the user to call other methods than
     * the default {@link BinaryOperator#operate(List<MathObject>)} from it. <br>
     * If you are looking forward to using the {@code operate()} method from an operator, use {@link #binaryCompute(MathObject, MathObject, Class)}
     * instead.
     * @param clazz the type of the operator
     * @param <T> the generic type to avoid casting
     * @return the supported operator whose {@link #getClass()} method equals the clazz parameter.
     */
    public <T extends MathOperator> T getBinaryOperator(Class<T> clazz) {
        for(MathOperator current : supportedOperators) {
            if(current.getClass().equals(clazz))
                return (T) current;
        }
        return null;
    }

    public SortingManager getSortingManager() {
        return sortingManager;
    }

    @FunctionalInterface
    private interface AlterationAction<T, S> {
        void action(T t, S s);
    }


    public static class Builder {

        private MaskContext context;

        public Builder() {
            this.context = new MaskContext();
        }

        public MaskContext build() {
            return context;
        }

        public Builder withPrecision(int precision) {
            context.bigDecimalContext = new MathContext(precision);
            return this;
        }

        public Builder withOperators(Function<MaskContext, MathOperator>... operators) {
            context.supportedOperators = Stream.of(operators).map(op -> op.apply(context)).collect(Collectors.toSet());
            return this;
        }

        public Builder withSortingManager(SortingManager manager) {
            context.sortingManager = manager;
            return this;
        }
    }
}
