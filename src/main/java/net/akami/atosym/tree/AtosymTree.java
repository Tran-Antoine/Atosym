package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import org.antlr.v4.runtime.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AtosymTree<T extends SimpleBranch> implements AbstractSyntaxTree<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);

    private Vocabulary vocabulary;

    private MaskContext context;
    private List<T> branches;

    public AtosymTree(MaskContext context, Vocabulary vocabulary) {
        this.context = context;
        this.branches = new ArrayList<>();
        this.vocabulary = vocabulary;
    }

    public void addBranch(T branch) {
        this.branches.add(branch);
    }

    @Override
    public MathObject merge() {
        for (T self : branches) {
            self.merge();
        }
        return finalResult();
    }

    protected MathObject finalResult() {
        T first = branches.get(branches.size()-1);
        if (first.hasSimplifiedValue()) {
            return first.getSimplifiedValue();
        }
        throw new RuntimeException("Internal error : Could not solve the given tree");
    }

    @Override
    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    @Override
    public MaskContext getContext() {
        return context;
    }

    /**
     * @return the iterator of the branches' list
     */
    @Override
    public Iterator<T> iterator() {
        return branches.iterator();
    }
}
