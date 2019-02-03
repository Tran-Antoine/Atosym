package net.akami.mask;

public class OperationOld {

    private static OperationOld[] operations;
    private static final OperationOld none = new OperationOld(' ');
    private final char character;
    private Function function;

    public OperationOld(char operation) {
        this.character = operation;
    }

    public OperationOld withFunction(Function function) {
        this.function = function;
        return this;
    }

    public static OperationOld none() {
        return none;
    }

    public char getChar() {
        return character;
    }

    public float compute(String a, String b) {
        return function.compute(Float.parseFloat(a), Float.parseFloat(b));
    }

    public static OperationOld getByCharacter(char c) {
        for(OperationOld op : operations) {
            if(op.character == c)
                return op;
        }
        return null;
    }

    public static void setOperations(OperationOld... operations) {
        OperationOld.operations = operations;
    }

    public static OperationOld[] getOperations() {
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

