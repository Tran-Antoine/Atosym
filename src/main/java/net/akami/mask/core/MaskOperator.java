package net.akami.mask.core;

import net.akami.mask.utils.ReducerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * A specific operation taking an input, sorting out an output. <br>
 * Available operators :
 * <ul>
 * <li> {@link MaskSimplifier}, a simple layer for the reducing tree system. It will soon replace the {@link ReducerFactory} class.
 * <li> {@link MaskDerivativeCalculator}, providing support for derivatives
 * <li> {@link MaskImageCalculator}, calculating images with a given function.
 * </ul>
 * @param <E> potential required additional data
 */
public interface MaskOperator<E> {

    /**
     * Computes a result from the given {@code in} parameter, writing the result into the mutable {@code out} parameter,
     * using the extra data required. Although some of the operators just need a single input being the {@code in} Mask,
     * most of them need extra information that cannot be taken out of the initial Mask, such as which char corresponds to
     * the variable (non-constant) for the derivatives calculator, or which variables should be replaced by which values
     * for the images calculator.
     * @param in the Mask which will be used as the basis to compute the result
     * @param out the Mask which will be used as the container for the computed result
     * @param extraData potential extra data required by the operator for each calculation
     * @param context the environment for the calculations. See {@link MaskContext} for further information
     */
    void compute(Mask in, Mask out, E extraData, MaskContext context);

    /**
     * Used to retrieve a list containing the default operators currently provided by the library. <br>
     * Operators supported by now :
     * <ul>
     * <li> {@link MaskSimplifier}
     * <li> {@link MaskDerivativeCalculator}
     * <li> {@link MaskImageCalculator}
     * </ul>
     * @return a list of MaskOperators
     */
    static List<MaskOperator> defaultOperators() {
        return Arrays.asList(
                new MaskSimplifier(),
                new MaskDerivativeCalculator(),
                new MaskImageCalculator()
        );
    }
}
