package net.akami.mask.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * A Binary tree handles branch splitting, evaluating and merging with the given behaviours. <br/> <br/>
 * When instantiating a BinaryTree, note that the splitting should automatically and instantly be performed
 * in the {@link BinaryTree#create(Branch)}, starting off with the given branch. <br/>
 * This {@link BinaryTree#create(Branch)} method must define how a branch must be divided according to ALL the splitters, whereas
 * {@link BinaryTree#split(Branch, char...)} defines how each branch must be divided, according to the splitter(s) given.
 * <br/> <br/>
 * In other words, the create method defines how and with which parameter the split method will be called.
 *
 * @param <T> what kind of branch will be handled by the tree.
 * @author Antoine Tran
 */
public abstract class BinaryTree<T extends Branch> implements Iterable<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private List<T> branches;
    private char[] splitters;

    public BinaryTree(String expression, char... splitters) {
        this.branches = new ArrayList<>();
        this.splitters = splitters;
        load(expression);
    }

    public T load(String expression) {
        T initial = generate(expression);
        branches.add(initial);
        create(initial);
        return initial;
    }

    /**
     * Defines how each branch must be split. Note that checks concerning size / values of the char array are
     * usually not needed, since the user is supposed to know what the array must be.
     * @param self the current branch not split yet
     * @param by the 'splitters', which are used to determine the left and right part of the branch
     */
    protected abstract void split(T self, char... by);
    protected abstract void create(T self);
    protected abstract T generate(String origin);

    /**
     * Note that if the branch type used hasn't redefined the canBeEvaluated method, you are guaranteed that
     * the branch has a left and a right part.
     * @param self
     */
    protected abstract void evalBranch(T self);

    public String merge() {
        /*
        If we give a very simple expression such as '50' to the reducer, it will detect that no operation
        needs to be done, and will simply calculate nothing. In this case, we return the expression itself.
         */
        if (getBranches().size() == 1 && !branches.get(0).canBeEvaluated()) {
            LOGGER.debug("Only one branch found. Returns it.");
            return getBranches().get(0).getExpression();
        }


        //Merging the branches from the last one to the first one (this initial expression)
        for (int i = getBranches().size() - 1; i >= 0; i--) {

            T self = getBranches().get(i);

            if (!self.canBeEvaluated()) {
                LOGGER.info("Not calculable : hasChildren : {} / children have no children : {}",
                        self.hasChildren(), self.doChildrenHaveChildren());
                continue;
            }

            evalBranch(self);

            if(finalResult().isPresent()) {
                return finalResult().get();
            }
        }
        return null;
    }

    public Optional<String> finalResult() {
        T first = getBranches().get(0);
        if (first.hasReducedValue()) {
            if (String.valueOf(first.getReducedValue()).equals("Infinity"))
                throw new ArithmeticException();

            return Optional.of(String.valueOf(first.getReducedValue()));
        }
        return Optional.empty();
    }

    @Override
    public final Iterator<T> iterator() {
        return branches.iterator();
    }

    public List<T> getBranches() {
        return branches;
    }

    public char[] getSplitters() {
        return splitters;
    }
}
