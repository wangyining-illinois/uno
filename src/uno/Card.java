package uno;

import static uno.Util.NONE_NUMBER;
import uno.Util.Color;
import uno.Util.Special;

/**
 * A Card Object represents a single card in UNO.
 */
public class Card {

    private Color color;
    private int number;
    private Special special;


    /**
     * Initialize a Card Object
     * @param newColor The color of the card, Color.NONE if the card is a wild card
     * @param newNumber The number of the card, -1 if the card has no number
     * @param newSpecial The special type of the card, Special.NONE is the card has number
     */
    public Card (Color newColor, int newNumber, Special newSpecial) {
        color = newColor;
        number = newNumber;
        special = newSpecial;
    }

    /**
     * Initialize a card with number
     * @param color The new color of the card
     * @param number The number of the card
     * @return a card object with number
     */
    public static Card createNormalCard(Color color, int number) {
        return new Card (color, number, Special.NONE);
    }

    /**
     * Initialize a card with special effects
     * @param color The new color of the card
     * @param special The special effect of the card
     * @return a card object with special
     */
    public static Card createSpecialCard(Color color, Special special) {
        return new Card (color, NONE_NUMBER, special);
    }

    public Color getColor() {
        return color;
    }
    public int getNumber() {
        return number;
    }
    public Special getSpecial() {
        return special;
    }

    /**
     * Print out the color, number, and special effect of the card to standard output
     */
    public void printCardInfo() {
        if (getColor() == Color.GREEN) System.out.print("Green ");
        if (getColor() == Color.RED) System.out.print("Red ");
        if (getColor() == Color.BLUE) System.out.print("Blue ");
        if (getColor() == Color.YELLOW) System.out.print("Yellow ");
        if (getNumber() != -1) {
            System.out.println(getNumber());
            return;
        }
        if (getSpecial() == Special.SKIP) System.out.println("Skip");
        if (getSpecial() == Special.REVERSE) System.out.println("Reverse");
        if (getSpecial() == Special.DRAWTWO) System.out.println("Draw Two");
        if (getSpecial() == Special.WILD) System.out.println("Wild");
        if (getSpecial() == Special.WILDDRAWFOUR) System.out.println("Wild Draw Four");
    }
}