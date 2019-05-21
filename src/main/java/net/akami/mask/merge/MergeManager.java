package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MergeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeManager.class);
    private final List<MergeBehavior> behaviors;

    public MergeManager(MaskContext context) {
        this.behaviors = loadBehaviors(context);
    }

    private List<MergeBehavior> loadBehaviors(MaskContext context) {
        return new ArrayList<>(Arrays.asList(
                new MonomialAdditionMerge(context),
                new VariableCombination(context),
                new PairNullifying(context)
        ));
    }

    public <T extends MergeBehavior> T getByType(Class<T> clazz) {
        for (MergeBehavior mergeBehavior : behaviors) {
            if (mergeBehavior.getClass().equals(clazz))
                return (T) mergeBehavior;
        }

        return null;
    }

    public <T> MergeBehavior<T> getByHandledType(Class<T> clazz) {
        for (MergeBehavior mergeBehavior : behaviors) {
            if (mergeBehavior.getHandledTypes().contains(clazz))
                return mergeBehavior;
        }
        return null;
    }

    public <T extends Comparable<T>, S extends T> List<S> merge(List<S> self, Class<T> clazz) {
        return merge(self, self, getByHandledType(clazz), true);
    }

    public <T extends Comparable<T>, S extends T> List<S> merge(List<S> l1, List<S> l2, Class<T> clazz) {
        return merge(l1, l2, getByHandledType(clazz), false);
    }

    public <T extends Comparable<T>, S extends T> List<S> merge(List<S> self, MergeBehavior<T> behavior) {
        return merge(self, self, behavior, true);
    }

    public <T extends Comparable<T>, S extends T> List<S> merge(List<S> l1, List<S> l2, MergeBehavior<T> behavior) {
        return merge(l1, l2, behavior, false);
    }

    public <T extends Comparable<T>, S extends T> List<S> secureMerge(List<S> l1, List<S> l2, MergeBehavior<T> behavior, boolean singleList) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), behavior, singleList);
    }

    public <T extends Comparable<T>, S extends T> List<S> secureMerge(List<S> l1, List<S> l2, Class<T> clazz) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), getByHandledType(clazz), false);
    }

    private <T extends Comparable<T>, S extends T> List<S> merge(List<S> l1, List<S> l2, MergeBehavior<T> behavior, boolean singleList) {
        List<S> finalResult = nonSortedMerge(l1, l2, behavior, singleList);
        Collections.sort(finalResult);
        return finalResult;
    }

    public <T, S extends T> List<S> nonSortedMerge(List<S> l1, List<S> l2, MergeBehavior<T> behavior, boolean singleList) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        List<S> finalResult = new ArrayList<>();
        int i = 0;
        for (S element : l1) {
            if (element == null) {
                i++;
                continue;
            }
            int j = 0;
            for (S element2 : l2) {

                if (singleList && i == j) { j++; continue; }
                if (element2 == null) { j++; continue; }

                if (behavior.isMergeable(element, element2)) {
                    T localResult = behavior.mergeElement(element, element2);
                    l1.set(i, null);
                    l2.set(j, null);

                    finalResult.add((S) localResult);
                    break;
                }
                j++;
            }
            i++;
        }
        finalResult.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if (!singleList)
            finalResult.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        return finalResult;
    }

    public void add(MergeBehavior self) {
        behaviors.add(self);
    }
}
