package de.ktran.anno1404warenrechner.helpers;

import java.util.Collection;

public class JavaCompat {

    /**
     * This is necessary since RetroLambda fails with forEach as the Consumer class is not included in old Android versions
     * @param collection iterable collection
     * @param action lambda
     * @param <T> collection item type
     */
    public static <T> void forEach(Collection<T> collection, CompatConsumer<T> action) {
        //noinspection Convert2streamapi
        for (T item : collection) action.accept(item);
    }
}
