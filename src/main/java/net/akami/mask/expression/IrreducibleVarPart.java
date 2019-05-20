package net.akami.mask.expression;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.utils.ExpressionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IrreducibleVarPart implements Variable<IrreducibleVarPart>, Cloneable {

    private final List<ExpressionEncapsulator> layers;
    private final List<ExpressionElement> elements;
    private String finalExpression;

    public IrreducibleVarPart(List<ExpressionElement> parts) {
        this(parts, Collections.emptyList());
    }

    public IrreducibleVarPart(List<ExpressionElement> parts, List<ExpressionEncapsulator> layers) {
        this.elements = Collections.unmodifiableList(Objects.requireNonNull(parts));
        this.layers = Collections.unmodifiableList(Objects.requireNonNull(layers));

        if(layers.size() == 0)
            throw new IllegalArgumentException
                    ("Cannot create a composed variable with no layer. Use multiple SimpleVariables instead");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IrreducibleVarPart)) return false;

        IrreducibleVarPart other = (IrreducibleVarPart) obj;

        if(this.length() != other.length()) return false;
        if(!layers.equals(other.layers)) return false;

        for(int i = 0; i < this.length(); i++) {
            ExpressionElement v1 = get(i);
            ExpressionElement v2 = other.get(i);

            if(!v1.equals(v2)) return false;
        }

        return true;
    }

    public int length() {
        return elements.size();
    }

    public ExpressionElement get(int index) {
        return elements.get(index);
    }

    @Override
    public String getExpression() {
        if(finalExpression == null)
            finalExpression = loadExpression();
        return finalExpression;
    }

    private String loadExpression() {
        return ExpressionUtils.encapsulate(elements, layers);
    }

    @Override
    public int compareTo(IrreducibleVarPart o) {
        return 0;
    }

    public List<ExpressionEncapsulator> getLayers() {
        return layers;
    }

    public List<ExpressionElement> getElements() {
        return elements;
    }

    public ExpressionEncapsulator getLayer(int i) {
        if(i < 0) return layers.get(layers.size()-i);
        else return layers.get(i);
    }

    @Override
    public Object clone() {
        return new IrreducibleVarPart(getElements());
    }
}
