package com.github.liamdev06.mc.nmsadvanced.utility;

import net.minecraft.server.v1_8_R3.EntityTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NMSUtil {

    /**
     * Taken from <a href="https://www.spigotmc.org/threads/tutorial-register-and-use-nms-entities-1-8.77607/">here</a>
     * Code has been slightly cleaned up from the original link
     *
     * @param name name of the entity
     * @param id entity id, find out more <a href="http://minecraft.gamepedia.com/Data_values/Entity_IDs">here</a>
     * @param customClass the custom entity
     */
    public static void registerEntity(String name, int id, Class<?> customClass) {
        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();

            for (Field field : EntityTypes.class.getDeclaredFields()) {
                if (field.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    field.setAccessible(true);
                    dataMap.add((Map<?, ?>) field.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
