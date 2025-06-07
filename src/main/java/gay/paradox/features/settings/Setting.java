package gay.paradox.features.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
@RequiredArgsConstructor
public class Setting {
    private final String name;
    private Predicate<Object> dependency;

    public boolean isVisible() {
        return dependency == null || dependency.test(null);
    }
}
