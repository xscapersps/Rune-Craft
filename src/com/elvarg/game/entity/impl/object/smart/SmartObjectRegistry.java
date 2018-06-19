package com.elvarg.game.entity.impl.object.smart;

import java.util.LinkedList;

public class SmartObjectRegistry {

    private static LinkedList<SmartObject> smartObjects = new LinkedList<>();

    public static void register(SmartObject object) {
        smartObjects.add(object);
    }

    public static LinkedList<SmartObject> getSmartObjects() {
        return smartObjects;
    }

}
