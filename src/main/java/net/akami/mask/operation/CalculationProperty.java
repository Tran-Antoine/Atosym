package net.akami.mask.operation;

public enum CalculationProperty {

    ANGLES_UNIT("trigonometryOperation", new DegreesMode(), new RadiansMode()),
    NUMERIC_FRACTIONS_TREATMENT("formatting"),
    POLYNOMIAL_FRACTIONS_TREATMENT("division"),
    POW_EXPANDING_LIMIT("pow"),
    SIGNIFICANT_DIGITS_AMOUNT("formatting"),
    CACHE_SIZE("calculationPerformed", new CacheSizeProperty());

    private CalculationMode[] elements;
    private CalculationMode current;
    private String binding;

    CalculationProperty(String binding, CalculationMode... elements) {
        this.binding = binding;
        this.elements = elements;
        this.current = elements[0];
    }

    public void setActiveMode(Class<? extends CalculationMode> type) {
        for(CalculationMode element : elements) {
            if (element.getClass().equals(type)) {
                current = element;
                return;
            }
        }
    }

    public CalculationMode getCurrentMode() {
        return current;
    }

    public String getBinding() {
        return binding;
    }

    public interface CalculationMode {

        boolean cancelsOperation(String self);

        String[] compute(String... self);

        CalculationProperty getAttachment();
    }
}
