package gay.paradox.features;

import gay.paradox.Paradox;
import gay.paradox.event.Subscriber;
import gay.paradox.features.settings.Setting;
import gay.paradox.utils.Imports;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Getter
public class Feature implements Subscriber, Imports {
    private final ArrayList<Setting> settings = new ArrayList<>();
    private boolean enabled;
    @Setter
    private int key;
    private final FeatureInfo info = this.getClass().getAnnotation(FeatureInfo.class);

    public Feature() {
        this.key = info != null ? info.key() : -1;
        this.initSettings();
    }

    public void initSettings() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value instanceof Setting) {
                    settings.add((Setting) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return info == null ? this.getClass().getSimpleName() : info.name();
    }

    public String getDescription() {
        return info == null ? "" : info.description();
    }

    public Category getCategory() {
        return info == null ? Category.CLIENT : info.category();
    }

    public void onEnable() {
        Paradox.EVENT_BUS.register(this);
    }

    public void onDisable() {
        Paradox.EVENT_BUS.unregister(this);
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean hasSettings() {
        return !settings.isEmpty();
    }

    public String getInfo() {
        return "";
    }
}
