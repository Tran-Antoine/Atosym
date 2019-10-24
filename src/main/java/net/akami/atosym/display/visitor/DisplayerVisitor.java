package net.akami.atosym.display.visitor;

import net.akami.atosym.display.FunctionalNotationDisplayable;
import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.display.PrefixNotationDisplayable;

public interface DisplayerVisitor {

    String accept(InfixNotationDisplayable displayable);
    String accept(FunctionalNotationDisplayable displayable);
    String accept(PrefixNotationDisplayable displayable);
    // TODO : 'Image accept(LatexDisplayer displayer);'


}
