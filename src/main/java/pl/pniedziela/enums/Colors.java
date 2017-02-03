package pl.pniedziela.enums;

import java.util.Stack;

/**
 * Created by Przemysław on 02.02.2017.
 */
public abstract class Colors {

    private final static Stack<String> colors;

    static {
        colors = new Stack<>();
        colors.push("WHITE");
        colors.push("PINK");
        colors.push("ORANGE");
        colors.push("PURPLE");
        colors.push("YELLOW");
        colors.push("GREEN");
        colors.push("RED");
        colors.push("BLUE");
    }

    public static String popColor() {
        return colors.pop();
    }

    public static void pushColor(String color) {
        colors.push(color);
    }
}
