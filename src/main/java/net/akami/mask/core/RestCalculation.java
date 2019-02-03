package net.akami.mask.core;

public class RestCalculation {

    private final String rest;

    public RestCalculation(String rest) {
        this.rest = rest;
    }

    public String asExpression() {
        return rest;
    }

    public int asInt() {
        return (int) Float.parseFloat(rest);
    }

    public float asFloat() {
        return Float.parseFloat(rest);
    }

    public MaskExpression asMaskExpression() {
        return new MaskExpression(rest);
    }
}
