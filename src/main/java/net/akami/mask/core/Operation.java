package net.akami.mask.core;


import java.util.ArrayList;
import java.util.List;

public class Operation {

    private static final List<Operation> operations = new ArrayList<>();

    private final char operation;
    private Function function;

    public Operation(char operation) {
        this.operation = operation;
        operations.add(this);
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public char getOperation() {
        return operation;
    }

    public int compute(String a, String b) {
        return function.compute(Integer.parseInt(a), Integer.parseInt(b));
    }

    public static Operation getByCharacter(char c) {
        for(Operation op : operations) {
            if(op.operation == c)
                return op;
        }
        return null;
    }
    public interface Function {

        int compute(int a, int b);
    }
}
