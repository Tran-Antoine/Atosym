package net.akami.mask.merge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MergeManager {

    private MergeManager() {}

    private static final List<MergeBehavior> behaviors = new ArrayList<>(Arrays.asList(
            new ElementAdditionMerge(),
            new FractionAdditionMerge(),
            new MonomialAdditionMerge(),
            new VariableCombination()
    ));

    public static <T extends MergeBehavior> T getByType(Class<T> clazz) {
        for(MergeBehavior mergeBehavior : behaviors) {
            if(mergeBehavior.getClass().equals(clazz))
                return (T) mergeBehavior;
        }

        return null;
    }

    public static <T> MergeBehavior<T> getByHandledType(Class<T> clazz) {
        for(MergeBehavior mergeBehavior : behaviors) {
            if(mergeBehavior.getHandledTypes().contains(clazz))
                return mergeBehavior;
        }
        return null;
    }

    public static <T> List<T> merge(List<T> self, Class<T> clazz) {
        return merge(self, self, getByHandledType(clazz),true);
    }

    public static <T> List<T> merge(List<T> l1, List<T> l2, Class<T> clazz) {
        return merge(l1, l2, getByHandledType(clazz),false);
    }

    public static <T> List<T> merge(List<T> self, MergeBehavior<T> behavior) {
        return merge(self, self, behavior,true);
    }

    public static <T> List<T> merge(List<T> l1, List<T> l2, MergeBehavior<T> behavior) {
        return merge(l1, l2, behavior,false);
    }

    private static <T> List<T> merge(List<T> l1, List<T> l2, MergeBehavior<T> behavior, boolean singleList) {
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        List<T> finalResult = new ArrayList<>();
        int i = 0;
        for(T element : l1) {
            if(element == null) {i++; continue;}
            int j = 0;
            for(T element2 : l2) {
                if(singleList && i == j) {j++; continue;}
                if(element2 == null) {j++;continue;}

                if(behavior.isMergeable(element, element2)) {
                    T localResult = behavior.mergeElement(element, element2);
                    l1.set(i, null);
                    l2.set(j, null);

                    finalResult.add(localResult);
                    break;
                }
                j++;
            }
            i++;
        }
        finalResult.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if(!singleList)
            finalResult.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return finalResult;
    }

    public static void add(MergeBehavior self) {
        behaviors.add(self);
    }
}
