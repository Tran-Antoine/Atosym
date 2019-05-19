package net.akami.mask.core;

import net.akami.mask.utils.ReducerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * A specific operation taking an input, sorting out an output. <p>
 * Available operators :
 * <li> {@link MaskReducer}, a simple layer for the reducing tree system. It will replace the {@link ReducerFactory} class.
 * <li> {@link MaskDerivativeCalculator}, providing support for derivatives
 * <li> {@link MaskImageCalculator}, calculating images with a given function.
 * @param <E> potential required additional data
 */
public interface MaskOperator<E> {

    void compute(MaskExpression in, MaskExpression out, E extraData, MaskContext context);

    static List<MaskOperator> defaultOperators() {
        return Arrays.asList(
                new MaskReducer(),
                new MaskDerivativeCalculator(),
                new MaskImageCalculator()
        );
    }
}
