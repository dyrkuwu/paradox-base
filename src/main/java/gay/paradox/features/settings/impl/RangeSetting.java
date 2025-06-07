package gay.paradox.features.settings.impl;

import gay.paradox.features.settings.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class RangeSetting extends Setting {
    private float minValue;
    private float maxValue;
    private float lowerBound;
    private float upperBound;
    private float increment;
    private String suffix = "";

    public RangeSetting(String name, float defaultMin, float defaultMax, float lowerBound, float upperBound, float increment) {
        super(name);
        this.minValue = defaultMin;
        this.maxValue = defaultMax;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.increment = increment;
    }

    public RangeSetting(String name, float defaultMin, float defaultMax, float lowerBound, float upperBound, float increment, String suffix) {
        this(name, defaultMin, defaultMax, lowerBound, upperBound, increment);
        this.suffix = suffix;
    }

    public RangeSetting(String name, float defaultMin, float defaultMax, float lowerBound, float upperBound, float increment, Predicate<Object> dependency) {
        this(name, defaultMin, defaultMax, lowerBound, upperBound, increment);
        setDependency(dependency);
    }

    public RangeSetting(String name, float defaultMin, float defaultMax, float lowerBound, float upperBound, float increment, String suffix, Predicate<Object> dependency) {
        this(name, defaultMin, defaultMax, lowerBound, upperBound, increment, suffix);
        setDependency(dependency);
    }
    
    public void incrementMin() {
        setMinValue(Math.min(maxValue, Math.min(upperBound, minValue + increment)));
    }
    
    public void decrementMin() {
        setMinValue(Math.max(lowerBound, minValue - increment));
    }
    
    public void incrementMax() {
        setMaxValue(Math.min(upperBound, maxValue + increment));
    }
    
    public void decrementMax() {
        setMaxValue(Math.max(minValue, Math.max(lowerBound, maxValue - increment)));
    }
    
    public float getMinPercentage() {
        return (minValue - lowerBound) / (upperBound - lowerBound);
    }
    
    public float getMaxPercentage() {
        return (maxValue - lowerBound) / (upperBound - lowerBound);
    }
    
    public float getRange() {
        return maxValue - minValue;
    }
}
