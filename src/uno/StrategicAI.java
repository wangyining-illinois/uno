package uno;
import uno.Util.Color;

/**
 * StrategicAI class extends BaselineAI and overrides strategies for playing card and choosing color.
 */
public class StrategicAI extends BaselineAI {

    /**
     * Play out playable number card first if there is any, then skip/reverse, then draw two/wild/wild draw four
     */
    @Override public int playCard(Util.Color currColor, int currNumber, Util.Special currSpecial, int penalty) {
        for (int i = 0; i < cards.size(); i++) {
            if (validate(i, currColor, currNumber, currSpecial, penalty) && cards.get(i).getSpecial() == Util.Special.NONE) {
                return i;
            }
        }
        for (int i = 0; i < cards.size(); i++) {
            if (validate(i, currColor, currNumber, currSpecial, penalty) && cards.get(i).getColor() != Color.NONE
                    && cards.get(i).getSpecial() != Util.Special.DRAWTWO) {
                return i;
            }
        }
        for (int i = 0; i < cards.size(); i++) {
            if (validate(i, currColor, currNumber, currSpecial, penalty)) return i;
        }
        return -1;
    }

    /**
     * choose the new color according to the colors of the hand cards.
     * @return the color that appears most in the hand cards
     */
    @Override public Util.Color chooseColor() {
        int redCount = 0;
        int greenCount = 0;
        int blueCount = 0;
        int yellowCount = 0;
        for (Card card : cards) {
            switch (card.getColor()) {
                case GREEN:
                    greenCount++;
                    break;
                case YELLOW:
                    yellowCount++;
                    break;
                case RED:
                    redCount++;
                    break;
                case BLUE:
                    blueCount++;
                    break;
            }
        }
        int max = Math.max(Math.max(redCount, greenCount), Math.max(blueCount, yellowCount));
        if (max == redCount) return Color.RED;
        if (max == greenCount) return Color.GREEN;
        if (max == blueCount) return Color.BLUE;
        return Color.YELLOW;
    }
}
