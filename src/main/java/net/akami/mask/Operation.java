package net.akami.mask;

public class Operation {

    private static Operation[] operations;
    private static final Operation none = new Operation(' ');
    private final char character;
    private Function function;

    public Operation(char operation) {
        this.character = operation;
    }

    public Operation withFunction(Function function) {
        this.function = function;
        return this;
    }

    public static Operation none() {
        return none;
    }

    public char getChar() {
        return character;
    }

    public float compute(String a, String b) {
        return function.compute(Float.parseFloat(a), Float.parseFloat(b));
    }

    public static Operation getByCharacter(char c) {
        for(Operation op : operations) {
            if(op.character == c)
                return op;
        }
        return null;
    }

    public static void setOperations(Operation... operations) {
        Operation.operations = operations;
    }

    public static Operation[] getOperations() {
        return operations;
    }
    public static char[] getSigns() {
        char[] signs = new char[operations.length];
        for(int i = 0; i<signs.length; i++) {
            signs[i] = operations[i].getChar();
        }
        return signs;
    }

    public static boolean isOperationSign(char c) {
        for(char sign : getSigns())
            if(sign == c)
                return true;
        return false;
    }
    public interface Function {

        float compute(float a, float b);
    }
}

