package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;
import net.akami.atosym.utils.ExpressionUtils;

import java.util.List;

public class MultMathObject extends MathFunction {

    public MultMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {

        StringBuilder builder = new StringBuilder();

        for(MathObject displayable : children) {
            fillBuilder(builder, displayable);
        }

        return builder.toString();
    }

    private void fillBuilder(StringBuilder self, MathObject displayable) {
        String currentDisplay = displayable.display();

        if(self.length() == 0) {
            self.append(currentDisplay);
            return;
        }

        char lastChar = self.charAt(self.length() - 1);
        char nextChar = currentDisplay.charAt(0);

        if(!validMultShortcut(lastChar, nextChar)) {
            self.append('*');
        }

        self.append(currentDisplay);
    }

    private boolean validMultShortcut(char a, char b) {
        return ExpressionUtils.isANumber(String.valueOf(a)) || ExpressionUtils.isANumber(String.valueOf(b));
    }
}
