package net.akami.mask.merge;

import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.overlay.property.DetailedMergeProperty;
import net.akami.mask.overlay.property.MergePacket;
import net.akami.mask.overlay.property.OverallMergeProperty;
import net.akami.mask.overlay.property.OverlayMergeProperty;
import net.akami.mask.utils.VariableComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OverlayMultiplicationMerge {

    private Monomial m1;
    private Monomial m2;
    private List<OverlayMergeProperty> properties;
    private boolean requestStartOver = false;

    public OverlayMultiplicationMerge(Monomial m1, Monomial m2, List<OverlayMergeProperty> properties) {
        this.m1 = m1;
        this.m2 = m2;
        this.properties = properties;
    }

    public List<Variable> merge() {
        List<Variable> l1 = new ArrayList<>(m1.getVarPart().getVariables());
        List<Variable> l2 = new ArrayList<>(m2.getVarPart().getVariables());
        return merge(l1, l2, false);
    }

    public <D extends MergePacket> List<Variable> merge(List<Variable> l1, List<Variable> l2, boolean singleList) {
        List<Variable> finalVars = new ArrayList<>();
        boolean restart = false;

        for(int i = 0; i < l1.size(); i++) {

            Variable v1 = l1.get(i);
            if(v1 == null) continue;

            for(int j = 0; j < l2.size(); j++) {
                if(singleList && i == j) continue;

                Variable v2 = l2.get(j);
                if(v2 == null) continue;

                for(OverlayMergeProperty property : properties) {

                    if(property instanceof OverallMergeProperty) {
                        OverallMergeProperty<List<Variable>, List<Variable>, D> overallProperty = (OverallMergeProperty) property;
                        Optional<D> packet = overallProperty.isApplicable(l1, l2);
                        if(packet.isPresent()) {
                            l1.set(i, null);
                            l2.set(j, null);
                            finalVars.addAll(overallProperty.result(m1, m2, packet.get()));
                            if(overallProperty.requiresStartingOver()) restart = true;
                            break;
                        }

                    }

                    if(property instanceof DetailedMergeProperty) {
                        DetailedMergeProperty detailedProperty = (DetailedMergeProperty) property;
                        Optional<MergePacket> packet = detailedProperty.isApplicableFor(v1, v2);
                        if(packet.isPresent()) {
                            l1.set(i, null);
                            l2.set(j, null);
                            detailedProperty.merge(v1, v2, finalVars, packet.get());
                            if(detailedProperty.requiresStartingOver()) restart = true;

                            break;
                        }
                    }
                }
            }
        }
        finalVars.addAll(l1.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        if (!singleList)
            finalVars.addAll(l2.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        finalVars.sort(VariableComparator.COMPARATOR);
        if(restart) {
            finalVars = merge(finalVars, finalVars, true);
        }
        return finalVars;
    }

    public boolean startingOverRequested() {
        return requestStartOver;
    }
}
