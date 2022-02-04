package it.uniroma2.dicii.isw2.deliverable1.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class, used to cmpare object, usually Tickets or Commits, based on a specific object method name
 */
public class CollectionSorter {
    /**
     * Sort a <code>List</code> of items, based on a result returned by a method.
     *
     * @param list   list to be sorted
     * @param method method whose return value is used as comparator
     */
    public static void sort(List<?> list, Method method) {
        Collections.sort(list, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                try {
                    return ((Comparable<Comparable>) method.invoke(o1)).compareTo((Comparable<?>) method.invoke(o2));
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
