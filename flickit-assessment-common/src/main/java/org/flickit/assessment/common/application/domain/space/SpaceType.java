package org.flickit.assessment.common.application.domain.space;

public enum SpaceType {
    PERSONAL,
    PREMIUM;

    public int getId() {
        return this.ordinal();
    }

    public static SpaceType valueOfById(int id) {
        if (!isValid(id))
            return null;
        return SpaceType.values()[id];
    }

    private static boolean isValid(int id) {
        return id >= 0 && id <= SpaceType.values().length;
    }
}
