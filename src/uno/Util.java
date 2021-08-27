package uno;

public class Util {

    public static final int INITIAL_PILE_CAPACITY = 108;

    public static final int NONE_NUMBER = -1;

    /**
     *This enum specifies all the possible special types of cards.
     */
    public enum Special {
        SKIP, REVERSE, DRAWTWO, WILD, WILDDRAWFOUR, NONE
    }

    /**
     * This enum specifies all the possible colors of Card.
     */
    public enum Color {
        GREEN, RED, YELLOW, BLUE, NONE
    }

    /**
     * This enum specifies the directions of the game.
     */
    public enum Direction {
        CLOCKWISE, COUNTER_CLOCKWISE
    }


}
