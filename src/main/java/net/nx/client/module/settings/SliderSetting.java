package net.nx.client.module.settings;

public class SliderSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public SliderSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setValue(Double value) {
        this.value = Math.max(min, Math.min(max, snapToStep(value)));
    }

    private double snapToStep(double value) {
        if (step <= 0) return value;
        return Math.round(value / step) * step;
    }

    public int getIntValue() { return (int) Math.round(value); }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }
    public double getPercent() { return (value - min) / (max - min); }
}
