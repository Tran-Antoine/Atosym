package net.akami.mask.core;

import net.akami.mask.affection.CalculationAlteration;
import net.akami.mask.expression.Expression;
import net.akami.mask.function.MathFunction;
import net.akami.mask.handler.AlterationHandler;
import net.akami.mask.handler.BinaryOperationHandler;
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
 * can result in different outcomes. You will for instance be able to decide whether {@code 5/2} will remain {@code 5/2} or
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
 * @author Antoine Tran
 */
public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext();

    private Set<BinaryOperationHandler<Expression>> binaryHandlers;
    private Set<MathFunction> supportedFunctions;
    private MathContext bigDecimalContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaskContext.class);

    public MaskContext() {
        this.binaryHandlers = BinaryOperationHandler.generateDefaultHandlers(this);
        this.supportedFunctions = MathFunction.generateDefaultFunctions(this);
        this.bigDecimalContext = new MathContext(150);
    }

    public <T> T binaryCompute(T a, T b, Class<? extends BinaryOperationHandler<T>> clazz) {
        BinaryOperationHandler<T> handler = getBinaryOperation(clazz);
        return handler.rawOperate(a, b);
    }

    public <T extends BinaryOperationHandler> T getBinaryOperation(Class<T> clazz) {

        for(BinaryOperationHandler current : binaryHandlers) {
            if(current.getClass().equals(clazz))
                return (T) current;
        }
        return null;
    }

    public Optional<MathFunction> getFunctionByBinding(char binding) {
        for(MathFunction function : supportedFunctions) {
            if(function.getBinding() == binding)
                return Optional.of(function);
        }
        return Optional.empty();
    }

    public Optional<MathFunction> getFunctionByExpression(String self) {
        for(char c : self.toCharArray()) {
            Optional<MathFunction> function = getFunctionByBinding(c);
            if(function.isPresent())
                return function;
        }
        return Optional.empty();
    }

    public void addHandler(BinaryOperationHandler<Expression> handler) {
        binaryHandlers.add(handler);
    }

    public void removeHandler(Class<? extends BinaryOperationHandler<Expression>> clazz) {
        for(BinaryOperationHandler<Expression> handler : binaryHandlers) {
            if(handler.getClass().equals(clazz)) {
                binaryHandlers.remove(handler);
                return;
            }
        }
        LOGGER.warn("Unable to remove handler from the given class name : {}", clazz.getName());
    }

    public void addFunction(MathFunction target) {
        supportedFunctions.add(target);
    }

    public void removeFunction(Class<? extends MathFunction> clazz) {
        for(MathFunction func : supportedFunctions) {
            if(func.getClass().equals(clazz)) {
                supportedFunctions.remove(func);
                return;
            }
        }
        LOGGER.warn("Unable to remove function from the given class name : {}", clazz.getName());
    }

    public MathContext getMathContext() {
        return bigDecimalContext;
    }
}
