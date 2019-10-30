package net.akami.atosym.expression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static net.akami.atosym.expression.MathProperty.*;

public enum MathObjectType {

    // Non functional types
    NUMBER, VARIABLE,
    // Binary operators types
    SUM(COMMUTATIVITY, ASSOCIATIVITY, SET_ENCLOSURE),
    SUB(ASSOCIATIVITY, SET_ENCLOSURE),
    MULT(COMMUTATIVITY, ASSOCIATIVITY, DISTRIBUTIVITY, SET_ENCLOSURE),
    DIV(DISTRIBUTIVITY, UNDEFINED_RESULT),
    POW(),
    ROOT(),
    // Trigonometrical types
    SIN(PERIODICITY), COS(PERIODICITY), TAN(PERIODICITY),
    // Other
    FACTORIAL(SET_RESTRICTION);

    private Set<MathProperty> properties;

    MathObjectType(MathProperty... properties) {
        this.properties = new HashSet<>(Arrays.asList(properties));
    }

    public boolean hasProperty(MathProperty property) {
        return properties.contains(property);
    }
}
