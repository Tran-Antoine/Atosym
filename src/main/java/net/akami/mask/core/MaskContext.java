package net.akami.mask.core;

import net.akami.mask.affection.CalculationAlteration;
import net.akami.mask.check.*;
import net.akami.mask.exception.MaskException;
import net.akami.mask.merge.property.MergePropertyManager;
import net.akami.mask.expression.Expression;
import net.akami.mask.function.MathFunction;
import net.akami.mask.handler.AlterationHandler;
import net.akami.mask.handler.BinaryOperationHandler;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.MathContext;
import java.util.*;

/**
 * One of the core objects of the Mask API. It defines a calculation environment, required to perform any
 * operation on expressions. <p>
 * A context (or environment) allows you to customize different calculations.
 * In fact, one might not have the same needs as one other. <p>
 * You are free to add some support for
 * mathematical patterns that are supported by the API, or, on the other hand, remove some undesired features provided
 * by default, which will make your calculations run faster. <p>
 * Furthermore, the environment gives you a total control over inputs and outputs, which means that a single expression
 * can merge in different outcomes. You will for instance be able to decide whether {@code 5/2} will remain {@code 5/2} or
 * will be reduced as {@code 2.5}. <p>
 * For further information about inputs and outputs alteration, see {@link CalculationAlteration} and
 * {@link AlterationHandler}.
 * <pre></pre>
 *
 * The MaskContext class handles :
 *
 *      <li> A set of {@link BinaryOperationHandler}s. Basically, the 5 default operations, being the addition, the
 *      subtraction, the multiplication, the division and the power calculation. Although this is not recommended,
 *      you are free to remove any of these operations, if you are guaranteed that they won't be required. However,
 *      because your expression doesn't literally contain a given operation doesn't mean it is not required. For
 *      instance, the power calculator requires the multiplier to work, since it basically chains multiplications a given
 *      amount of times. <p>
 *      Note : binary operations take care of the affection system. You can directly modify the handlers, or add your owns
 *      with different affections. <p>
 *      <li> A set of {@link MathFunction}s. Only the mathematical functions present in the set wil be supported.
 *      Mathematical functions can require multiple arguments. See {@link MathFunction}'s documentation for further
 *      information <p>
 *      <li> A {@link MathContext}, used to define the amount of significant digits for calculations.
 *  </pre>
 *
 * 
 * @author Antoine Tran
 */
public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();

    private Set<BinaryOperationHandler<Expression>> binaryHandlers;
    private Set<MathFunction> supportedFunctions;
    private List<ValidityCheck> validityChecks;
    private MergePropertyManager propertyManager;
    private MergeManager mergeManager;
    private MathContext bigDecimalContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskContext.class);

    /**
     * Constructs a context with the default parameters. The set of {@link BinaryOperationHandler}s will be generated
     * from {@link BinaryOperationHandler#generateDefaultHandlers(MaskContext)}, the set of mathematical functions
     * will be generated from {@link MathFunction#generateDefaultFunctions(MaskContext)}, and the MathContext will
     * use a 100 digits precision.
     */
    public MaskContext() {
        this(100);
    }

    /**
     * Constructs a context with the given precision. <p>
     * See {@link MaskContext#MaskContext()} for further information concerning the other handled data.
     * @param precision the amount of significant digits handled by the context
     */
    public MaskContext(int precision) {
        this.binaryHandlers = BinaryOperationHandler.generateDefaultHandlers(this);
        this.supportedFunctions = MathFunction.generateDefaultFunctions(this);
        this.bigDecimalContext = new MathContext(precision);
        this.propertyManager = new MergePropertyManager(this);
        this.mergeManager = new MergeManager(this);
        this.validityChecks = defaultValidityChecks();
    }

    /**
     * Performs a calculation from the two elements given and the selected operator. <p>
     * By default (recommended), the 5 operations (+-{@literal *}/^) are available. This method is generally called
     * from the {@link MathUtils} class
     * @param a the first element to compute with the second one
     * @param b the second element to compute with the first one
     * @param clazz the type of the binary operator in charge of the calculation
     * @param <T> the binary operator's handled type. All default operators use the {@link Expression} type
     * @return a result computed by the operator from the two elements given
     */
    public <T> T binaryCompute(T a, T b, Class<? extends BinaryOperationHandler<T>> clazz) {
        BinaryOperationHandler<T> handler = getBinaryOperation(clazz);
        return handler.rawOperate(a, b);
    }

    /**
     * Returns the instance of a required operator, allowing the user to call other methods than
     * the default {@link BinaryOperationHandler#operate(Object, Object)} from it. <p>
     * If you are looking forward to using the {@code operate()} method from an operator, use {@link #binaryCompute(Object, Object, Class)}
     * instead.
     * @param clazz the type of the operator
     * @param <T> the generic type to avoid casting
     * @return the supported operator whose {@link #getClass()} method equals the clazz parameter.
     */
    public <T extends BinaryOperationHandler> T getBinaryOperation(Class<T> clazz) {
        for(BinaryOperationHandler current : binaryHandlers) {
            if(current.getClass().equals(clazz))
                return (T) current;
        }
        return null;
    }

    /**
     * Returns the mathematical function matching the given binding. Since two functions are equals as long as their
     * binding is similar, there can be only one function matching it, because the functions are grouped in a set.
     * @param binding the given binding, supposed to match a single function present in the set
     * @return an {@link Optional#empty()} if the binding does not match anything, otherwise {@link Optional#of(Object)},
     * the parameter being the matched function.
     */
    public Optional<MathFunction> getFunctionByBinding(char binding) {
        for(MathFunction function : supportedFunctions) {
            if(function.getBinding() == binding)
                return Optional.of(function);
        }
        return Optional.empty();
    }

    /**
     * Goes through the given input, and tries to match every char with a mathematical function. The first matched function
     * found is returned.
     * @param self the given input
     * @return the first function that matches a char from the input
     */
    public Optional<MathFunction> getFunctionByExpression(String self) {
        for(char c : self.toCharArray()) {
            Optional<MathFunction> function = getFunctionByBinding(c);
            if(function.isPresent())
                return function;
        }
        return Optional.empty();
    }

    /**
     * Adds a new handler to the set. Only {@link BinaryOperationHandler}s with {@link Expression} as generic type
     * are supported.
     * @param handler a new handler to add
     */
    public void addHandler(BinaryOperationHandler<Expression> handler) {
        binaryHandlers.add(handler);
    }

    /**
     * Removes the handler from the set whose {@link #getClass()} method equals the clazz parameter
     * @param clazz the class type to remove
     */
    public void removeHandler(Class<? extends BinaryOperationHandler<Expression>> clazz) {
        for(BinaryOperationHandler<Expression> handler : binaryHandlers) {
            if(handler.getClass().equals(clazz)) {
                binaryHandlers.remove(handler);
                return;
            }
        }
        LOGGER.warn("Unable to remove handler from the given class name : {}", clazz.getName());
    }

    /**
     * Adds a new function to the set. <p>
     * Note that the new function will replace any existing function whose binding would match with the new function's.
     * @param target a new function to add
     */
    public void addFunction(MathFunction target) {
        supportedFunctions.add(target);
    }

    /**
     * Removes the function from the set whose {@link #getClass()} method equals the clazz parameter
     * @param clazz the class type to remove
     */
    public void removeFunction(Class<? extends MathFunction> clazz) {
        for(MathFunction func : supportedFunctions) {
            if(func.getClass().equals(clazz)) {
                supportedFunctions.remove(func);
                return;
            }
        }
        LOGGER.warn("Unable to remove function from the given class name : {}", clazz.getName());
    }

    /**
     * @return the {@link MathContext} instance, used to define the calculation precision
     */
    public MathContext getMathContext() {
        return bigDecimalContext;
    }

    /**
     * @return the merge manager handled
     */
    public MergeManager getMergeManager() {
        return mergeManager;
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

    public void assertExpressionValidity(Mask self) {
        for(ValidityCheck check : validityChecks) {
            if(!check.isValid(self.getExpression()))
                throw new MaskException(check.errorMessage(self.getExpression()), self);
        }
    }
}
