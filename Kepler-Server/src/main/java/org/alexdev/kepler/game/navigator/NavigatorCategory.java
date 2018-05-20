package org.alexdev.kepler.game.navigator;

public class NavigatorCategory {
    private int id;
    private String name;
    private boolean publicSpaces;
    private boolean allowTrading;
    private int minimumRoleAccess;
    private int minimumRoleSetFlat;

    public NavigatorCategory(int id, String name, boolean publicSpaces, boolean allowTrading, int minimumRoleAccess, int minimumRoleSetFlat) {
        this.id = id;
        this.name = name;
        this.publicSpaces = publicSpaces;
        this.allowTrading = allowTrading;
        this.minimumRoleAccess = minimumRoleAccess;
        this.minimumRoleSetFlat = minimumRoleSetFlat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPublicSpaces() {
        return publicSpaces;
    }

    public boolean hasAllowTrading() {
        return allowTrading;
    }

    public int getMinimumRoleAccess() {
        return minimumRoleAccess;
    }

    public int getMinimumRoleSetFlat() {
        return minimumRoleSetFlat;
    }
}
