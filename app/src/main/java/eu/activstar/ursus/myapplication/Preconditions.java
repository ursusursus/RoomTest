package eu.activstar.ursus.myapplication;

import java.util.Collection;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 08-Jun-17.
 */
public class Preconditions {

    public static <T> T checkNotNull(T t) {
        if (t == null) throw new NullPointerException();
        return t;
    }

    public static int checkNotZeroOrNegative(int number) {
        if (number <= 0) throw new IllegalArgumentException("Number has to be greater than 0");
        return number;
    }

    public static float checkNotZeroOrNegative(float number) {
        if (number <= 0) throw new IllegalArgumentException("Number has to be greater than 0");
        return number;
    }

    public static int checkNotNegative(int number) {
        if (number < 0) throw new IllegalArgumentException("Number cannot be negative");
        return number;
    }

    public static <T extends Collection<E>, E> T checkNotNullAndNonEmpty(T collection) {
        checkNotNull(collection);
        if (collection.isEmpty()) throw new IllegalStateException("Collection needs to be non-empty");
        return collection;
    }
}
