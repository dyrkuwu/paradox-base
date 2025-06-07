package gay.paradox.features.settings.impl;

import gay.paradox.features.settings.Setting;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class BoolSetting extends Setting {
    private Boolean value;

    public BoolSetting(String name, boolean defaultValue) {
        super(name);
        this.value = defaultValue;
    }

    public BoolSetting(String name, boolean defaultValue, Predicate<Object> dependency) {
        this(name, defaultValue);
        setDependency(dependency);
    }
}
