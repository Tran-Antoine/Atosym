package net.akami.mask.operation;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MaskHandlerTest {

    private MaskHandler handler = new MaskHandler();

    @Test
    public void reducerTest() {
        prepare("(x+y)^2");
        assertOperator(MaskReducer.class, null, "x^2.0+y^2.0+2.0xy");
        end();
    }

    @Test
    public void derivativeTest() {
        prepare("4x^2+xy");
        assertOperator(MaskDerivativeCalculator.class, 'x', "8x+y");
        end();
    }

    @Test
    public void imageTest() {
        prepare("4x^2-5y");
        Map<Character, String> images = new HashMap<>();
        images.put('x', "2");
        // TODO : implement a better comparison that would make sure that the expression doesn't start with '-'
        assertOperator(MaskImageCalculator.class, images, "-5.0y+16.0");
    }

    private void prepare(String in) {
        handler.begin(new MaskExpression(in));
    }

    private <E, T extends MaskOperator<E>> void assertOperator(Class<T> type, E extraData, String result) {
        Assertions.assertThat(handler.compute(type, null, extraData).asExpression())
            .isEqualTo(result);
    }

    private void end() {
        handler.end();
    }
}
