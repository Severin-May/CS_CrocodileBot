package crocodile;

public enum Commands {
    START("/start"),
    HELP("/help"),
    SEE("/see"),
    NEXT("/next"),
    MENU("/menu"),
    ABOUT("/about");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static Commands fromString(String text) {
        for (Commands command : Commands.values()) {
            if (command.command.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null; // or throw an IllegalArgumentException
    }
}

