package net.akami.atosym.expression;

public interface MathObject {

    String display();
    MathObject operate();
    MathObjectType getType();
}
