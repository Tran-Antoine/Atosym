package net.akami.mask.operation;

import net.akami.mask.operation.CalculationProperty.CalculationMode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaskContext {

    public static final MaskContext DEFAULT = new MaskContext(Stream.of(CalculationProperty.values())
            .collect(Collectors.toSet()));
    private Set<CalculationProperty> properties;

    public MaskContext() {
        this(new HashSet<>());
    }

    public MaskContext(Set<CalculationProperty> properties) {
        this.properties = properties;
    }

    public CalculationMode bind(String binding) {
        return getProperty(binding).getCurrentMode();
    }

    public void addProperty(CalculationProperty property) {
        properties.add(property);
    }

    public CalculationProperty getProperty(String binding) {
        for (CalculationProperty property : properties) {
            if (property.getBinding().equals(binding))
                return property;
        }
        return null;
    }

}
