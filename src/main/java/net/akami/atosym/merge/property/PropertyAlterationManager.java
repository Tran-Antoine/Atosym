package net.akami.atosym.merge.property;

import net.akami.atosym.alteration.CalculationCanceller;
import net.akami.atosym.alteration.IOCalculationModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyAlterationManager {

    private Map<Class, AlterationPack> alterationsMap;

    public PropertyAlterationManager() {
        this.alterationsMap = new HashMap<>();
    }

    public <T, R> void addCancellerFor(Class<? extends OverallMergeProperty<T, R>> clazz, CalculationCanceller<T, R> alteration) {
        if(alterationsMap.containsKey(clazz)) {
            AlterationPack<T, R> pack = (AlterationPack<T, R>) alterationsMap.get(clazz);
            pack.cancellers.add(alteration);
        } else {
            List<IOCalculationModifier<T>> modifiers = new ArrayList<>();
            List<CalculationCanceller<T, R>> cancellers = new ArrayList<>();
            cancellers.add(alteration);
            alterationsMap.put(clazz, new AlterationPack<>(modifiers, cancellers));
        }
    }
    public <T, R> void addModifierFor(Class<? extends OverallMergeProperty<T, R>> clazz, IOCalculationModifier<T> alteration) {
        if(alterationsMap.containsKey(clazz)) {
            AlterationPack<T, R> pack = (AlterationPack<T, R>) alterationsMap.get(clazz);
            pack.modifiers.add(alteration);
        } else {
            List<IOCalculationModifier<T>> modifiers = new ArrayList<>();
            List<CalculationCanceller<T, R>> cancellers = new ArrayList<>();
            modifiers.add(alteration);
            alterationsMap.put(clazz, new AlterationPack<>(modifiers, cancellers));
        }
    }

    public static class AlterationPack<T, R> {

        private List<IOCalculationModifier<T>> modifiers;
        private List<CalculationCanceller<T, R>> cancellers;

        private AlterationPack(List<IOCalculationModifier<T>> modifiers, List<CalculationCanceller<T, R>> cancellers) {
            this.modifiers = modifiers;
            this.cancellers = cancellers;
        }

        public List<IOCalculationModifier<T>> getModifiers() {
            return modifiers;
        }

        public List<CalculationCanceller<T, R>> getCancellers() {
            return cancellers;
        }
    }
}
