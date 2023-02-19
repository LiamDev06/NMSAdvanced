package com.github.liamdev06.mc.nmsadvanced.utility.autoregistry;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.reflections.Reflections;

import java.util.*;

/**
 * Automatically initialize the constructor for classes that are marked with {@link AutoRegister}
 *
 * @author Liam
 */
public class AutoRegistry {

    /**
     * Initialize a new constructor instance for the class specified
     *
     * @param clazz the class to initialize the constructor for
     * @param parameter if the class has another class parameter (like the main {@link NMSPlugin} instance)
     * @param initArg the value of the parameter, usually the object instance of {@link NMSPlugin}
     */
    public static void register(@NonNull Class<?> clazz, @Nullable Class<?> parameter, @Nullable Object initArg) {
        try {
            if (parameter == null && initArg == null) {
                // Initialize the new instance without any parameters
                clazz.getConstructor().newInstance();
            } else {
                // Initialize the new instance with parameters
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
     * @return all classes that have the {@link AutoRegister} annotation with the specified type
     */
    public static Class<?>[] getClassesWithRegisterType(@NonNull Type type) {
        List<Class<?>> classes = new ArrayList<>();
        Reflections reflections = new Reflections(NMSPlugin.class.getPackage().getName());

        // Using the Reflections library to find all classes in this project annotated with AutoRegister
        Set<Class<?>> foundClasses = reflections.getTypesAnnotatedWith(AutoRegister.class);

        for (Class<?> clazz : foundClasses) {
            AutoRegister autoRegister = clazz.getAnnotation(AutoRegister.class);

            // The auto register type value is equal to the type the method is looking for, add it to the return list
            if (autoRegister.type() == type) {
                classes.add(clazz);
            }
        }

        // Return as a new array list
        return classes.toArray(new Class[0]);
    }

    /**
     * Annotation element for the @AutoRegister annotation to categorize them
     */
    public enum Type {
        COMMAND,
        CUSTOM_ENTITY,
        LISTENER
    }
}







