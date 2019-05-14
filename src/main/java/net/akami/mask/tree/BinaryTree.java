package net.akami.mask.tree;

import net.akami.mask.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * A Binary tree handles branch splitting, evaluating and merging with the defined behaviours. <br/> <br/>
 * When instantiating a BinaryTree, note that the splitting should automatically and instantly be performed
 * in the {@link BinaryTree#begin(Branch)}, starting off with the given branch. <br/>
 * This {@link BinaryTree#begin(Branch)} method must define how a branch must be divided according to ALL the splitters, whereas
 * {@link BinaryTree#split(Branch, char...)} defines how each branch must be divided, according to the splitter(s) given.
 * <br/> <br/>
 * In other words, the begin method defines how and with which parameter the split method will be called.
 *
 * Note that BinaryTree implements {@link Iterable}, so that you can actually use for each loops with it.
 *
 * @param <T> what kind of branch will be handled by the tree.
 * @author Antoine Tran
 */
public abstract class BinaryTree<T extends Branch> implements Iterable<T> {

    public static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);
    private List<T> branches;
    private char[] splitters;

    /**
     * Available constructor for any binary tree. Note that as soon as the tree is created, the splitting will
     * automatically begin.
     * @param expression the initial expression that forms the top of the tree
     * @param splitters the chars used to split the tree, see {@link BinaryTree#begin(Branch)} for further information.
     */
    public BinaryTree(String expression, char... splitters) {
        this.branches = new ArrayList<>();
        this.splitters = splitters;
        load(expression);
    }

    /**
     * Loads a new branch for the given expression. Note that {@link BinaryTree#generate(String)} should
     * never be used outside binary classes, since it should only return a branch from the given expression, whereas
     * the load() method performs necessary actions for splitting the tree.
     * @param expression
     * @return a branch created from the expression given
     */
    public T load(String expression) {
        T initial = generate(expression);
        branches.add(initial);
        begin(initial);
        return initial;
    }

    /**
     * Defines how the splitting of a defined branch must be planned. <br/>
     * In other words, begin must call the split() method one or more times according to the different splitters.
     * @param self the branch itself
     */
    protected abstract void begin(T self);

    /**
     * Defines how each branch must be split. Note that checks concerning size / values of the char array are
     * usually not needed, since the user is supposed to know what the array must be.
     * @param self the current branch not split yet
     * @param by the 'splitters', which are used to determine the left and right part of the branch
     *
     * @return whether the branch could be split with the chars given or not
     */
    protected abstract boolean split(T self, char... by);

    /**
     * Allows the class itself to instantiate branches from a given expression
     * @param origin the string the branch must be based on
     * @return a branch getting along with the kind of tree being used, from the given origin
     */
    protected abstract T generate(String origin);

    /**
     * Defines how a branch must be evaluated.
     * Note that if the branch type used hasn't redefined the {@code canBeEvaluated} method, you are guaranteed that
     * the branch has a left and a right part.
     * @param self the branch itself
     */
    protected abstract void evalBranch(T self);

    /**
     * Merges the whole tree. The usual behavior is to go from the last branch to the first one,
     * see if the current actually is calculable, and if yes then calls the {@link BinaryTree#evalBranch(Branch)}
     * method. <br/>
     * If the finalResult method does not return an empty optional, the value found is returned <br/>
     * Note that the merge method can be redefined if the behavior does not suits the tree.
     * @return the reduced value of the first branch, while finalResult() is not redefined
     */
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
                LOGGER.info("Not calculable : ");
                self.setReducedValue(new Expression(self.getExpression()));
                continue;
            }

            evalBranch(self);
            if(self.getReducedValue() != null && self.getReducedValue().equals("Infinity"))
                throw new ArithmeticException("Infinity value found");

            if(finalResult().isPresent()) {
                return finalResult().get();
            }
        }
        return null;
    }

    /**
     * Defines whether the final findResult has been calculated or not.
     * @return the final findResult if calculated, otherwise an empty optional.
     */
    public Optional<String> finalResult() {
        T first = getBranches().get(0);
        if (first.hasReducedValue()) {
            return Optional.of(String.valueOf(first.getReducedValue()));
        }
        return Optional.empty();
    }

    /**
     *
     * @return the iterator of the branches' list
     */
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
