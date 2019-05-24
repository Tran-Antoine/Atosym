package net.akami.mask.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class SimpleMerge<T> {

    private SimpleMerge() {}

    private BiFunction<T, T, Boolean> startOverFunction;
    private MergeBehavior<? super T> behavior;
    private List<T> l1;
    private List<T> l2;
    private boolean singleList;
    private boolean requestStartOver = false;

    public List<T> merge() {
        return merge(l1, l2);
    }

    public List<T> secureMerge() {
        return merge(new ArrayList<>(l1), new ArrayList<>(l2));
    }

    @SuppressWarnings("unchecked")
    private List<T> merge(List<T> l1, List<T> l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        List<T> finalResult = new ArrayList<>();
        int i = 0;
        for (T element : l1) {
            if (element == null) {
                i++;
                continue;
            }
            int j = 0;
            for (T element2 : l2) {

                if (singleList && i == j) { j++; continue; }
                if (element2 == null) { j++; continue; }

                if (behavior.isMergeable(element, element2)) {
                    T localResult = (T) behavior.mergeElement(element, element2);
                    l1.set(i, null);
                    l2.set(j, null);

                    finalResult.add(localResult);
                    if(requestStartOver) {
                        requestStartOver = false;
                        i = 0;
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

        if(!finalResult.isEmpty() && finalResult.get(0) instanceof Comparable) {
            Collections.sort((List<Comparable>) finalResult);
        }
        return finalResult;
    }

    public void requestStartOver() {
        this.requestStartOver = true;
    }

    public static final class Builder<T> {

        private SimpleMerge<T> object;

        public Builder() {
            this.object = new SimpleMerge<>();
        }

        public Builder<T> withStartOverFunction(BiFunction<T, T, Boolean> startOverFunction) {
            object.startOverFunction = startOverFunction;
            return this;
        }

        public Builder<T> withMergeBehavior(MergeBehavior<? super T> behavior) {
            object.behavior = behavior;
            return this;
        }

        public Builder<T> withSingleList(List<T> singleList) {
            object.l1 = singleList;
            object.l2 = singleList;
            object.singleList = true;
            return this;
        }

        public Builder<T> withLists(List<T> l1, List<T> l2) {
            object.l1 = l1;
            object.l2 = l2;
            object.singleList = false;
            return this;
        }

        public SimpleMerge<T> build() {
            String error = "Attempting to build a merge object without all its parameters being set";
            Objects.requireNonNull(object.startOverFunction, error);
            Objects.requireNonNull(object.behavior, error);
            // Makes sure that the merge remains unmodifiable after creation
            SimpleMerge<T> finalObject = object;
            this.object = null;
            return finalObject;
        }
    }
}
