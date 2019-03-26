package net.akami.mask.operation;

import net.akami.mask.operation.CalculationProperty.CalculationMode;

public class CacheSizeProperty implements CalculationMode {

    private int size;

    @Override
    public boolean cancelsOperation(String self) {
        return false;
    }

    @Override
    public CalculationProperty getAttachment() {
        return CalculationProperty.CACHE_SIZE;
    }

    @Override
    public String[] compute(String... self) {
        return null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
