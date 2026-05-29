package model;

/**
 * Generic label/number pair for chart datasets
 * (top sellers, sales-by-category, daily sales, top waste).
 */
public class NameValue {
    private final String label;
    private final double value;

    public NameValue(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public double getValue() { return value; }
}
