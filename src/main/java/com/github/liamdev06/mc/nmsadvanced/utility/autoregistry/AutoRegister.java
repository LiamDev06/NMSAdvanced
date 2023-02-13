package com.github.liamdev06.mc.nmsadvanced.utility.autoregistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically initialize objects that are marked with this (@AutoRegister) annotation
 *
 * @author Liam
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRegister {

    AutoRegistry.Type type();
    int entityId() default 0;

}
