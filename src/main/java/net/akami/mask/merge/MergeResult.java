package net.akami.mask.merge;

import java.util.Collections;
import java.util.List;

public class MergeResult<T> {

    public final List<T> result;
    public final boolean requestsStartingOver;

    public MergeResult(T result, boolean requestsStartingOver) {
        this.result = Collections.singletonList(result);
        this.requestsStartingOver = requestsStartingOver;
    }

    public MergeResult(List<T> result, boolean requestsStartingOver) {
        this.result = result;
        this.requestsStartingOver = requestsStartingOver;
    }
}
