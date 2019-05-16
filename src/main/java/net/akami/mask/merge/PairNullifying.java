package net.akami.mask.merge;

import java.util.Collections;
import java.util.Set;

public class PairNullifying implements MergeBehavior<Object> {

    @Override
    public boolean isMergeable(Object a, Object b) {
        return a.equals(b);
    }

    @Override
    public Object mergeElement(Object a, Object b) {
        return null;
    }

    @Override
    public Set<Class<?>> getHandledTypes() {
        return Collections.singleton(Object.class);
    }
}
