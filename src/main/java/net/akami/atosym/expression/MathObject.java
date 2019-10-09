package net.akami.atosym.expression;

public interface MathObject extends Comparable<MathObject> {

    String display();
    MathObjectType getType();
    int priority();
}
