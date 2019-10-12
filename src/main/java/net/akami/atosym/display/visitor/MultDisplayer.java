package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.utils.ExpressionUtils;

import java.util.List;

public class MultDisplayer extends SimpleDisplayerVisitor {

    public MultDisplayer(List<MathObject> children) {
        super(children, "mult");
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        StringBuilder builder = new StringBuilder();

        for (MathObject child : children) {
            fillBuilder(builder, child, displayable);
        }

        return builder.toString();
    }
    private void fillBuilder(StringBuilder self, MathObject child, InfixNotationDisplayable displayable) {
        String currentDisplay = child.getDisplayer().accept(displayable);

        if (self.length() == 0) {
            self.append(currentDisplay);
            return;
        }

        char lastChar = self.charAt(self.length() - 1);
        char nextChar = currentDisplay.charAt(0);

        if (!validMultShortcut(lastChar, nextChar)) {
            self.append('*');
        }

        self.append(currentDisplay);
    }

    private boolean validMultShortcut(char a, char b) {
        return !ExpressionUtils.isANumber(String.valueOf(a)) || !ExpressionUtils.isANumber(String.valueOf(b));
    }
}
