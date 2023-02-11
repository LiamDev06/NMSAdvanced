package com.github.liamdev06.mc.nmsadvanced.utility.autoregistry;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import org.reflections.Reflections;

import java.util.*;

/**
 * Automatically initialize objects that are marked with the @AutoRegister annotation
 *
 * @author Liam, not from course
 */
public class AutoRegistry {

    private static final Map<Class<?>, Object> REGISTRY = new HashMap<>();

    public static void register(Class<?> clazz) {
        try {
            REGISTRY.put(clazz, clazz.getConstructor().newInstance());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void register(Class<?> clazz, Class<?> constructor, Object value) {
        try {
            REGISTRY.put(clazz, clazz.getConstructor(constructor).newInstance(value));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Object getInstance(Class<?> clazz) {
        return REGISTRY.get(clazz);
    }

    /**
     * Make sure to not include "me.liamhbest.mc.nmsadvanced" in the packageAdd, only the addon
     */
    public static Class<?>[] getClassesWithRegisterType(Type type) {
        List<Class<?>> classes = new ArrayList<>();
        Reflections reflections = new Reflections(NMSPlugin.class.getPackage().getName());
        Set<Class<?>> foundClasses = reflections.getTypesAnnotatedWith(AutoRegister.class);

        for (Class<?> clazz : foundClasses) {
            if (clazz.isAnnotationPresent(AutoRegister.class)) {
                AutoRegister autoRegister = clazz.getAnnotation(AutoRegister.class);

                if (autoRegister.type() == type) {
                    classes.add(clazz);
                }
            }
        }

        return classes.toArray(new Class[0]);
    }

    public enum Type {
        COMMAND
    }
}







