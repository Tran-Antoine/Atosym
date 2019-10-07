package net.akami.atosym.core;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Highest level object of the operation architecture. <br>
 * It corresponds to the manager for the {@link MaskOperator} instances. It allows the user to call the {@code compute()} methods with
 * the {@link MaskOperator} wanted, using class types as parameters. <br>
 *
 * Four data are handled at once by the manager. The first one is the default atosym to use whenever the {@code in}
 * parameter is not specified through the {@code compute()} methods. Setting the default atosym's value (by calling {@link #begin(Mask)})
 * is optional, though errors will be thrown if {@code compute()} is being called without {@code in} specified. <br>
 * The second one is the calculation environment ({@link MaskContext}) to use for every calculation performed through the manager.
 * If no context is specified, {@link MaskContext#DEFAULT} will be used. <br>
 * The third one is a list of {@link MaskOperator}s. Those are the operators that are supported by the manager. Most of the time
 * this field doesn't require any modification. You'll only need to modify the list if you want to remove operators to limit
 * the features of the manager, or if you want to modify / addBranch new operators. <br>
 * Eventually, the fourth data is a boolean defining whether the {@code out} Mask should be set to the default Mask after
 * calls to {@code compute}. If set to true, it allows the user to chain operation without having to repeat the {@code in} Mask. <br>
 *
 * For further information, see {@link #compute(Class, Mask, Object)}, {@link #compute(Class, Mask, Mask, Object)}, {@link #compute(Class, Mask, Object, Consumer)}
 * and {@link #compute(Class, Mask, Mask, Object, Consumer)}
 */
public class MaskOperatorHandler {

    public static final MaskOperatorHandler DEFAULT = new MaskOperatorHandler();

    private Mask currentIn;
    private MaskContext context;
    private List<MaskOperator> operators;
    private boolean setToOut = true;

    /**
     * Constructs the manager with the default context and the default operators
     */
    public MaskOperatorHandler() {
        this(MaskContext.DEFAULT);
    }

    /**
     * Constructs the manager with the given context
     * @param context the given context
     */
    public MaskOperatorHandler(MaskContext context) {
        this.context = context;
        this.operators = MaskOperator.defaultOperators();
    }

    /**
     * Sets the "default Mask" to the given value. Default masks are used when {@code in} parameters are omitted when
     * calling {@code compute()}
     * @param current the atosym to set as the default atosym
     * @return current the instance itself for chaining
     */
    public MaskOperatorHandler begin(Mask current) {
        this.currentIn = current;
        return this;
    }

    /**
     * Defines whether {@code out} masks must be set as the values after computation. True by default
     * @param setToOut whether {@code out} masks must be set as default masks
     */
    public void setCurrentToOut(boolean setToOut) {
        this.setToOut = setToOut;
    }

    /**
     * Computes a string based on the {@code default} mask, according to the operator type given. <br>
     * Once the result is computed, the {@code out} mask will be reloaded with it through {@link Mask#reload(String)}. <br>
     * If no mask is set as the default {@code in}, an error will be thrown. Default masks can be set through {@link #begin(Mask)},
     * or after any {@code compute()} call if {@code setToOut} is set to true. <br>
     * The extra data corresponds to the additional information required by the operator to work. See {@link MaskOperator} for
     * further information.
     * @param op the type of the operator
     * @param out where the result computed will be written
     * @param extraData extra data required by the operator
     * @param <T> the type of operator, such as {@code MaskSimplifier.class}, {@code MaskDerivativeCalculator.class}, etc
     * @param <E> the type of extra data required by the operator
     * @return the operator itself for chaining
     */
    public <E, T extends MaskOperator<E>> MaskOperatorHandler compute(Class<T> op, Mask out, E extraData) {
        return compute(op, currentIn, out, extraData, null);
    }

    /**
     * Computes a string based on the {@code in} mask, according to the operator type given. <br>
     * Once the result is computed, the {@code out} mask will be reloaded with it through {@link Mask#reload(String)}. <br>
     * The extra data corresponds to the additional information required by the operator to work. See {@link MaskOperator} for
     * further information.
     * @param op the type of the operator
     * @param in the mask containing the expression the operator must base itself on.
     * @param out where the result computed will be written
     * @param extraData extra data required by the operator
     * @param <T> the type of operator, such as {@code MaskSimplifier.class}, {@code MaskDerivativeCalculator.class}, etc
     * @param <E> the type of extra data required by the operator
     * @return the operator itself for chaining
     */
    public <E, T extends MaskOperator<E>> MaskOperatorHandler compute(Class<T> op, Mask in, Mask out, E extraData) {
        return compute(op, in, out, extraData,null);
    }

    /**
     * Computes a string based on the {@code default} mask, according to the operator type given. <br>
     * Once the result is computed, the {@code out} mask will be reloaded with it through {@link Mask#reload(String)}. <br>
     * If no mask is set as the default {@code in}, an error will be thrown. Default masks can be set through {@link #begin(Mask)},
     * or after any {@code compute()} call if {@code setToOut} is set to true. <br>
     * The extra data corresponds to the additional information required by the operator to work. See {@link MaskOperator} for
     * further information. <br>
     * A consumer is included so that the user can perform actions based on the computed result.
     * @param op the type of the operator
     * @param out where the result computed will be written
     * @param extraData extra data required by the operator
     * @param outAction an action to perform with the {@code out} Mask after being reloaded.
     * @param <T> the type of operator, such as {@code MaskSimplifier.class}, {@code MaskDerivativeCalculator.class}, etc
     * @param <E> the type of extra data required by the operator
     * @return the operator itself for chaining
     */
    public <E, T extends MaskOperator<E>> MaskOperatorHandler compute(Class<T> op, Mask out,
                                                                      E extraData, Consumer<Mask> outAction) {
        return compute(op, currentIn, out, extraData, outAction);
    }

    /**
     * Computes a string based on the {@code in} mask, according to the operator type given. <br>
     * Once the result is computed, the {@code out} mask will be reloaded with it through {@link Mask#reload(String)}. <br>
     * The extra data corresponds to the additional information required by the operator to work. See {@link MaskOperator} for
     * further information. <br>
     * A consumer is included so that the user can perform actions based on the computed result.
     * @param op the type of the operator
     * @param in the mask containing the expression the operator must base itself on.
     * @param out where the result computed will be written
     * @param extraData extra data required by the operator
     * @param outAction an action to perform with the {@code out} Mask after being reloaded.
     * @param <T> the type of operator, such as {@code MaskSimplifier.class}, {@code MaskDerivativeCalculator.class}, etc
     * @param <E> the type of extra data required by the operator
     * @return the operator itself for chaining
     */
    public <E, T extends MaskOperator<E>> MaskOperatorHandler compute(Class<T> op, Mask in, Mask out,
                                                                      E extraData, Consumer<Mask> outAction) {
        Objects.requireNonNull(in);

        if(out == null)
            out = Mask.TEMP;

        MaskOperator<E> operator = findByType(op);
        operator.compute(in, out, extraData, this.context);
        if(outAction != null) {
            outAction.accept(out);
        }

        if(setToOut)
            this.currentIn = out;

        return this;
    }

    /**
     * Used to retrieve the operator matching the given type
     * @param type the type of the operator wanted
     * @return the operator matching the parameter
     */
    private <E> MaskOperator<E> findByType(Class<? extends MaskOperator<E>> type) {
        for(MaskOperator operator : operators) {
            if(operator.getClass().equals(type))
                return operator;
        }
        return null;
    }

    /**
     * @return the expression of the default Mask. If no default mask is defined, an exception will be thrown
     */
    public String asExpression() {
        Objects.requireNonNull(currentIn);
        return currentIn.getExpression();
    }

    public float asNumber() {
        Objects.requireNonNull(currentIn);
        return Float.parseFloat(currentIn.getExpression());
    }

    /**
     * Sets the current default value to null. Used to make sure that the mask can not me modified by accident
     */
    public void end() {
        this.currentIn = null;
    }
}
