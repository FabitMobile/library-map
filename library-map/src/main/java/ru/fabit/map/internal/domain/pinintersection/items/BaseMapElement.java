package ru.fabit.map.internal.domain.pinintersection.items;

public class BaseMapElement {

    private final String id;
    private Object userData;
    private String stateGroupIdentifier; // if one element from group changes state - the other change

    BaseMapElement(String id) {
        this.id = id;
    }

    public String getStateGroupIdentifier() {
        return stateGroupIdentifier;
    }

    public void setStateGroupIdentifier(String stateGroupIdentifier) {
        this.stateGroupIdentifier = stateGroupIdentifier;
    }

    public String getId() {
        return id;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
