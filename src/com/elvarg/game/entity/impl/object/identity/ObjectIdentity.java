package com.elvarg.game.entity.impl.object.identity;

import com.elvarg.game.definition.ObjectDefinition;
import com.elvarg.util.Misc;

public abstract class ObjectIdentity {

    /** See bottom of file for an explanation. **/
    public final boolean matchesIdentity(int objectID) {
        ObjectDefinition objectDefinition = ObjectDefinition.forId(objectID);
        return Misc.arrayHasElement(getIDCriteria(), objectID) && !Misc.arrayHasElement(getIDExceptionCriteria(), objectID)
                || isActionMatch(objectDefinition) || isFullNameMatch(objectDefinition) || isPartialNameMatch(objectDefinition);
    }

    private boolean isActionMatch(ObjectDefinition objectDefinition) {
        if(getActionCriteria() != null || getNotContainingActionCriteria() != null) {
            for (String interaction : objectDefinition.interactions) {
                if(Misc.arrayHasElement(getActionCriteria(), interaction) && !Misc.arrayHasElement(getNotContainingActionCriteria(), interaction)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPartialNameMatch(ObjectDefinition objectDefinition) {
        String objectName = objectDefinition.getName();
        if(getStartsWithCriteria() != null) {
            for (String name : getStartsWithCriteria()) {
                if(name.equalsIgnoreCase(objectName)) {
                    if(!Misc.arrayHasElement(getNotContainingCriteria(), objectName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFullNameMatch(ObjectDefinition objectDefinition) {
        String objectName = objectDefinition.getName();
        if(Misc.arrayHasElement(getFullNameCriteria(), objectName)) {
            return !Misc.arrayHasElement(getNotContainingCriteria(), objectName);
        }
        return false;
    }

    /**
     * Matching criteria priority is displayed below in descending accuracy.
     * ID & IDEX > Action & !Action > FullName & Containing > StartsWith & !Containing
     * These priorities are ordered based on what is most likely to return accurate results.
     * **/
    protected int[] getIDCriteria() {
        return null;
    }
    protected int[] getIDExceptionCriteria() {
        return null;
    }
    protected String[] getActionCriteria() {
        return null;
    }
    protected String[] getNotContainingActionCriteria() {
        return null;
    }
    protected String[] getFullNameCriteria() {
        return null;
    }
    protected String[] getStartsWithCriteria() {
        return null;
    }
    protected String[] getNotContainingCriteria() {
        return null;
    }

}
