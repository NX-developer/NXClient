package net.nx.client.module;

public enum Category {
    RENDER("Render"),
    HUD("HUD"),
    ANIMATION("Animation"),
    HYPIXEL("Hypixel"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
