package net.akami.mask.handler;

import net.akami.mask.affection.IOCalculationModifier;

import java.util.Collections;
import java.util.List;

public interface IOModificationHandler extends AffectionHandler<IOCalculationModifier, String[]> {

    @Override
    default String[] findResult(String[]... input) {
        List<IOCalculationModifier> affections = compatibleAffectionsFor(input);
        Collections.sort(affections);
        for(IOCalculationModifier affection : affections) {
            // TODO input = affection.modify(input);
        }

        return null;//input;
    }
}
