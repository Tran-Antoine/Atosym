package net.akami.atosym.alteration;

import net.akami.atosym.handler.AlterationHandler;

/**
 * Describes alteration handlers with two identical generic parameters
 */
public interface FairAlterationHandler<T> extends AlterationHandler<T, T> {
}
