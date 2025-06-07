package gay.paradox.features.settings.impl;

import gay.paradox.features.settings.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class IntSetting extends Setting {
    private int value;
    private int minValue;
    private int maxValue;
    private String suffix = "";

    public IntSetting(String name, int defaultValue, int minValue, int maxValue) {
        super(name);
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public IntSetting(String name, int defaultValue, int minValue, int maxValue, String suffix) {
        this(name, defaultValue, minValue, maxValue);
        this.suffix = suffix;
    }

    public IntSetting(String name, int defaultValue, int minValue, int maxValue, Predicate<Object> dependency) {
        this(name, defaultValue, minValue, maxValue);
        setDependency(dependency);
    }

    public IntSetting(String name, int defaultValue, int minValue, int maxValue, String suffix, Predicate<Object> dependency) {
        this(name, defaultValue, minValue, maxValue, suffix);
        setDependency(dependency);
    }
}