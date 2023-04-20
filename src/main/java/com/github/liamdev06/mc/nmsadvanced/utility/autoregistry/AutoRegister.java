package com.github.liamdev06.mc.nmsadvanced.utility.autoregistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Automatically initialize objects that are marked with this {@link AutoRegister} annotation
 *
 * @author Liam
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRegister {

    /**
     * Categorizes the different possible auto registering classes by setting a type element
     *
     * @return the type element specified in the annotation
     */
    AutoRegistry.Type type();

    /**
     * Only used when auto registering custom entities as they require an entity id when being registered via NMS
     *
     * @return the entity id element specified in the annotation
     */
    int entityId() default 0;

}
