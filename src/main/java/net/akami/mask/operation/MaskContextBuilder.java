package net.akami.mask.operation;


public class MaskContextBuilder {

    private MaskContext context;

    public MaskContextBuilder() {
        context = new MaskContext();
    }

    public MaskContextBuilder withAdditionalProperties(CalculationProperty... properties) {
        for(CalculationProperty property : properties)
            context.addProperty(property);
        return this;
    }
    public MaskContext build() {
        return context;
    }
}
