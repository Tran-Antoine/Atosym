package net.akami.atosym.display.visitor;

import net.akami.atosym.display.FunctionalNotationDisplayable;
import net.akami.atosym.display.InfixNotationDisplayable;

public interface DisplayerVisitor {

    String accept(InfixNotationDisplayable displayer);
    String accept(FunctionalNotationDisplayable displayable);
    // TODO : 'Image accept(LatexDisplayer displayer);'
}
