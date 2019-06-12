package net.akami.mask.merge;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Divider;
import net.akami.mask.merge.property.MergeData;
import net.akami.mask.merge.property.OverallMergePropertyUnused;
import net.akami.mask.merge.property.OverlayMergeProperty;
import net.akami.mask.utils.VariableUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OverlayDivisionMerge {

    private Monomial m1;
    private Monomial m2;
    private MaskContext context;

    public OverlayDivisionMerge(Monomial m1, Monomial m2, MaskContext context, List<OverlayMergeProperty> properties) {
        this.m1 = m1;
        this.m2 = m2;
        this.context = context;
        this.properties = properties;
    }

    private List<OverlayMergeProperty> properties;

    public <D extends MergeData> List<Monomial> merge() {

        for(OverlayMergeProperty property : properties) {

            if(property instanceof OverallMergePropertyUnused) {
                OverallMergePropertyUnused<Monomial, List<Monomial>, D> overallProperty = (OverallMergePropertyUnused<Monomial, List<Monomial>, D>) property;
                Optional<D> packet = overallProperty.isApplicable(m1, null);
                if(packet.isPresent()) {
                    return overallProperty.result(m1, m2, packet.get());
                }
                packet = overallProperty.isApplicable(m2, null);
                if(packet.isPresent()) {
                    return overallProperty.result(m2, m1, packet.get());
                }
            }
        }

        List<Variable> finalNumVars = VariableUtils.dissociate(m1.getVarPart());
        List<Variable> finalDenVars = VariableUtils.dissociate(m2.getVarPart());

        Divider divider = context.getBinaryOperation(Divider.class);
        return Collections.singletonList(divider.simpleDivision(m1.getNumericValue(), m2.getNumericValue(), finalNumVars, finalDenVars));
    }
}
