package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtosymTree<T extends SimpleBranch> implements Iterable<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private MaskContext context;
    private T top;

    public AtosymTree(MaskContext context) {
        this.context = context;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void setInitialBranch(T top) {
        this.top = top;
    }
    /**
     *
     * @return the iterator of the branches' list
     */
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public MaskContext getContext() {
        return context;
    }
}
