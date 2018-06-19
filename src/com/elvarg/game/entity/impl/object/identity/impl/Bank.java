package com.elvarg.game.entity.impl.object.identity.impl;

import com.elvarg.game.entity.impl.object.identity.ObjectIdentity;

public class Bank extends ObjectIdentity {

    @Override
    protected String[] getStartsWithCriteria() {
        return new String[] {
                "bank"
        };
    }

    @Override
    protected String[] getFullNameCriteria() {
        return new String[] {
                "bank booth", "bank chest"
        };
    }

    @Override
    protected String[] getNotContainingCriteria() {
        return new String[] {
                "closed"
        };
    }

    @Override
    protected String[] getActionCriteria() {
        return new String[] {
                "bank"
        };
    }

}
