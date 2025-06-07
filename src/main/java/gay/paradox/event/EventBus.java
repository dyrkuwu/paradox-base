package gay.paradox.event;

import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class EventBus {
    private final ArrayList<Subscriber> subscribers = new ArrayList<>();

    public void post(@NotNull Event event) {
        ArrayList<Method> methods = new ArrayList<>();
        
        for (Subscriber subscriber : subscribers) {
            methods.addAll(Arrays.stream(subscriber.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Subscribe.class))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].equals(event.getClass()))
                .toList());
        }

        methods.sort(Comparator.comparingInt(m -> m.getDeclaredAnnotation(Subscribe.class).priority()));

        for (Method method : methods) {
            try {
                if (!method.trySetAccessible()) continue;

                Object instance = subscribers.stream()
                    .filter(subscriber -> subscriber.getClass().equals(method.getDeclaringClass()))
                    .findFirst()
                    .orElse(null);

                if (instance != null) {
                    method.invoke(instance, event);
                    if (event.isCancelled()) return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(Subscriber subscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber);
    }

    public void unregister(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
