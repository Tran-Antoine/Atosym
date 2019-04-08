package net.akami.mask.operation;

import net.akami.mask.affection.MaskContext;

import java.util.Arrays;
import java.util.List;

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
