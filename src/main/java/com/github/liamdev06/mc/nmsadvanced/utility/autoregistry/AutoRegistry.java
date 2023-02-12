package com.github.liamdev06.mc.nmsadvanced.utility.autoregistry;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.reflections.Reflections;

import java.util.*;

/**
 * Automatically initialize the constructor for classes that are marked with the @AutoRegister annotation
 *
 * @author Liam, not from course
 */
public class AutoRegistry {

    /**
     * Initialize a new constructor instance for the class specified
     *
     * @param clazz the class to initialize the constructor for
     * @param parameter if the class has another class parameter (like the main NMSPlugin instance)
     * @param initArg the value of the parameter, usually the object instance of NMSPlugin
     */
    public static void register(@NonNull Class<?> clazz, @Nullable Class<?> parameter, @Nullable Object initArg) {
        try {
            if (parameter == null && initArg == null) {
                clazz.getConstructor().newInstance();
            } else {
                clazz.getConstructor(parameter).newInstance(initArg);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Will get an array list of all classes with a certain annotation and annotation element.
     *
     * @param type the annotation element to look for
     * @return all classes that have the AutoRegister annotation with the specified type
     */
    public static Class<?>[] getClassesWithRegisterType(@NonNull Type type) {
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







