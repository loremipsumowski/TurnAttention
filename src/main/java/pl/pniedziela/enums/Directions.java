package pl.pniedziela.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Przemysław on 02.02.2017.
 */
public enum Directions {
    UP, RIGHT, DOWN, LEFT;

    private static final List<Directions> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Directions randomDirection() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Directions getOppositeForDirection(Directions direction) {
        switch (direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return null;
        }
    }
}
