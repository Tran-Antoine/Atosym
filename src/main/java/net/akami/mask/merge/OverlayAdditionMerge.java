package net.akami.mask.merge;

import net.akami.mask.expression.Monomial;
import net.akami.mask.overlay.property.MergePacket;
import net.akami.mask.overlay.property.OverallMergeProperty;

import java.util.List;
import java.util.Optional;

public class OverlayAdditionMerge {

    private Monomial m1;
    private Monomial m2;
    private List<OverallMergeProperty> properties;
    private boolean requestStartOver = false;

    public OverlayAdditionMerge(Monomial m1, Monomial m2, List<OverallMergeProperty> properties) {
        this.m1 = m1;
        this.m2 = m2;
        this.properties = properties;
    }

    public <D extends MergePacket> Optional<List<Monomial>> merge() {
        for(OverallMergeProperty<Monomial, List<Monomial>, D> overallProperty : properties) {
            Optional<D> packet = overallProperty.isApplicable(m1, m2);
            if(packet.isPresent()) {
                this.requestStartOver = overallProperty.requiresStartingOver();
                return Optional.of(overallProperty.result(m1, m2, packet.get()));
            }
        }
        return Optional.empty();
    }

    public boolean startingOverRequested() {
        return requestStartOver;
    }
}
