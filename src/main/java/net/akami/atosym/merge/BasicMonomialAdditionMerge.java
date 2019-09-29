package net.akami.atosym.merge;

import net.akami.atosym.core.MaskContext;



/**
 * A {@link MonomialAdditionMerge} with a property replacement, being {@link CommonDenominatorAdditionProperty} to
 * {@link BasicCommonDenominatorAdditionProperty}. Used for division only, which does not require any extra division after computing.
 */
public class BasicMonomialAdditionMerge extends MonomialAdditionMerge {

    public BasicMonomialAdditionMerge(MaskContext context) {
        super(context);
    }

}
