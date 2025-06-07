package gay.paradox.features;

public enum Category {
    COMBAT("Combat"),
    EXPLOIT("Exploit"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    MISC("Misc"),
    CLIENT("Client");

    public String name = "";

    Category(final String name) {
        this.name = name;
    }
}
