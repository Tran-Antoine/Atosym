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
                new VariableCombinationBehavior(context),
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
            if (mergeBehavior.getHandledType().equals(clazz))
                return mergeBehavior;
        }
        return null;
    }

    public <S extends Comparable<S>> List<S> merge(List<S> self, Class<? super S> clazz) {
        return merge(self, self, getByHandledType(clazz), true);
    }

    public <S extends Comparable<S>> List<S> merge(List<S> l1, List<S> l2, Class<? super S> clazz) {
        return merge(l1, l2, getByHandledType(clazz), false);
    }

    public <S extends Comparable<S>> List<S> merge(List<S> self, MergeBehavior<? super S> behavior) {
        return merge(self, self, behavior, true);
    }
    public <S> List<S> merge(List<S> self, MergeBehavior<? super S> behavior, Comparator<S> comparator) {
        return merge(self, self, behavior, true, comparator);
    }

    public<S extends Comparable<S>> List<S> merge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior) {
        return merge(l1, l2, behavior, false);
    }

    public <S extends Comparable<S>> List<S> secureMerge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), behavior, singleList);
    }
    public <S> List<S> secureMerge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList, Comparator<S> comparator) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), behavior, singleList, comparator);
    }

    public <S> List<S> secureMerge(List<S> l1, List<S> l2, Class<? super S> clazz, Comparator<S> comparator) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), getByHandledType(clazz), false, comparator);
    }

    public <S extends Comparable<S>> List<S> secureMerge(List<S> l1, List<S> l2, Class<? super S> clazz) {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2), getByHandledType(clazz), false);
    }
    public <S> List<S> merge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList, Comparator<S> comparator) {
        List<S> finalResult = nonSortedMerge(l1, l2, behavior, singleList);
        finalResult.removeAll(Collections.singleton(null));
        finalResult.sort(comparator);
        return finalResult;
    }

    public <S extends Comparable<S>> List<S> merge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList) {
        List<S> finalResult = nonSortedMerge(l1, l2, behavior, singleList);
        finalResult.removeAll(Collections.singleton(null));
        Collections.sort(finalResult);
        return finalResult;
    }

    public <S> List<S> nonSortedSecuredMerge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList) {
        return nonSortedMerge(new ArrayList<>(l1), new ArrayList<>(l2), behavior, singleList);
    }
    @SuppressWarnings("unchecked")
    public <S> List<S> nonSortedMerge(List<S> l1, List<S> l2, MergeBehavior<? super S> behavior, boolean singleList) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        boolean requestsStartingOver = false;
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
                    MergeResult<S> rawResult = (MergeResult<S>) behavior.mergeElement(element, element2);
                    List<S> localResult = rawResult.result;
                    l1.set(i, null);
                    l2.set(j, null);

                    finalResult.addAll(localResult);
                    if(rawResult.requestsStartingOver) {
                        requestsStartingOver = true;
                    }
                    break;
                }
                j++;
            }
            i++;
        }
        finalResult.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if (!singleList)
            finalResult.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        if(requestsStartingOver) {
            finalResult = nonSortedMerge(finalResult, finalResult, behavior, true);
        }
        return finalResult;
    }

    public void add(MergeBehavior self) {
        behaviors.add(self);
    }
}
