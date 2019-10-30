package net.akami.atosym.operator;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.ParserUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class OperatorTestUtils {

    public static void assertBinaryOperation(String a, String b, String result, BinaryOperator operator) {
        MathObject aObject = toMathObject(a);
        MathObject bObject = toMathObject(b);
        assertThat(operator.binaryOperate(aObject, bObject).testDisplay()).isEqualTo(result);
    }

    public static MathObject toMathObject(String input) {
        return ParserUtils.generateSimpleTree(input).merge();
    }
}
