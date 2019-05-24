package net.akami.mask.merge;

import net.akami.mask.expression.Monomial;
import net.akami.mask.overlay.property.OverallMergeProperty;
import net.akami.mask.overlay.property.OverlayMergeProperty;

import java.util.List;
import java.util.Optional;

public class OverlayAdditionMerge {

    private Monomial m1;
    private Monomial m2;
    private List<OverlayMergeProperty> properties;
    private boolean requestStartOver = false;

    public OverlayAdditionMerge(Monomial m1, Monomial m2, List<OverlayMergeProperty> properties) {
        this.m1 = m1;
        this.m2 = m2;
        this.properties = properties;
    }


    public Optional<List<Monomial>> merge() {
        for(OverlayMergeProperty property : properties) {
            if(!(property instanceof OverallMergeProperty)) continue;
            OverallMergeProperty overallProperty = (OverallMergeProperty) property;
            if(overallProperty.isApplicable(m1, m2)) {
                this.requestStartOver = overallProperty.requiresStartingOver();
                return Optional.of(overallProperty.result(m1, m2));
            }
        }
        return Optional.empty();
    }

    public boolean startingOverRequested() {
        return requestStartOver;
    }
}
