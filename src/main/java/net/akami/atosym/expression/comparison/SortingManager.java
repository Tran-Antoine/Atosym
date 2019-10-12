package net.akami.atosym.expression.comparison;

import net.akami.atosym.expression.MathObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SortingManager implements Comparator<MathObject> {

    public static final SortingManager DEFAULT = new SortingManager(generateRules());
    private static final Logger LOGGER = LoggerFactory.getLogger(SortingManager.class);

    private final Set<SortingRule> rules;

    public SortingManager(Set<SortingRule> rules) {
        this.rules = rules;
    }

    public static Set<SortingRule> generateRules() {
        return new HashSet<>(Arrays.asList(
                new SimpleVariableSorting(),
                new NumberSorting()
        ));
    }

    @Override
    public int compare(MathObject o1, MathObject o2) {
        for(SortingRule rule : rules) {
            if(rule.isRuleSuitable(o1, o2)) {
                return rule.compare(o1, o2);
            }
        }
        //throw new IllegalStateException("Unreachable statement : Unable to compare the two objects");
        LOGGER.warn("Could not compare the two objects : {} and {}", o1, o2);
        return 0;
    }

    public void clearRules() {
        rules.clear();
    }

    public void addRule(SortingRule rule) {
        rules.add(rule);
    }

    public void removeRule(Class<? extends SortingRule> clazz) {
        for(SortingRule rule : rules) {
            if(rule.getClass().equals(clazz)) {
                rules.remove(rule);
                return;
            }
        }
    }
}
