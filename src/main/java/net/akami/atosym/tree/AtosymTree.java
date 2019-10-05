package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AtosymTree<T extends SimpleBranch> implements Iterable<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private MaskContext context;
    private Vocabulary vocabulary;
    private CommonTokenStream tokenStream;
    private List<T> branches;

    public AtosymTree(MaskContext context, Vocabulary vocabulary, CommonTokenStream tokenStream) {
        this.context = context;
        this.branches = new ArrayList<>();
        this.vocabulary = vocabulary;
        this.tokenStream = tokenStream;
    }

    public void add(T branch) {
        this.branches.add(branch);
    }
    /**
     *
     * @return the iterator of the branches' list
     */
    @Override
    public Iterator<T> iterator() {
        return branches.iterator();
    }

    public MaskContext getContext() {
        return context;
    }

    public MathObject merge() {

        //Merging the branches from the last one to the first one (this initial expression)
        for (T self : branches) {

            self.merge();

            Optional<MathObject> result = finalResult();
            if(result.isPresent()) {
                return result.get();
            }
        }

        throw new RuntimeException("Internal error : Could not solve the given tree");
    }

    public Optional<MathObject> finalResult() {
        T first = branches.get(branches.size()-1);
        if (first.hasSimplifiedValue()) {
            return Optional.of(first.getSimplifiedValue());
        }
        return Optional.empty();
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }
}
