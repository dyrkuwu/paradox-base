package gay.paradox.utils;

import java.security.SecureRandom;

public final class MathUtil {
    private static final SecureRandom random = new SecureRandom();

    public static int randomInt(int start, int bound) {
        return random.nextInt(start, bound);
    }

    public static double randomDouble(double start, double end) {
        return start + (end - start) * random.nextDouble();
    }

    public static double randomFloat(float start, float end) {
        return start + (end - start) * random.nextFloat();
    }
}
