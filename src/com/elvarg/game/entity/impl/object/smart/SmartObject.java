package com.elvarg.game.entity.impl.object.smart;

import com.elvarg.game.entity.impl.object.GameObject;
import com.elvarg.game.entity.impl.object.handle.ClickEventHandle;

public class SmartObject extends GameObject {

    private GameObject binding;
    private ClickEventHandle actions;

    private SmartObject(GameObject binding, ClickEventHandle actions) {
        super(binding.getId(), binding.getPosition(), binding.getType(), binding.getType());
        this.binding = binding;
        this.actions = actions;
        SmartObjectRegistry.register(this);
    }

    public SmartObject(GameObject binding) {
        this(binding, null);
    }

    public ClickEventHandle getActions() {
        return actions;
    }

    public GameObject getBinding() {
        return binding;
    }

}
