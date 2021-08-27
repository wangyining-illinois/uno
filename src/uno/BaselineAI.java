package uno;

import java.util.Random;

import static uno.Util.NONE_NUMBER;

/**
 * BaselineAI class inherits Player class.
 * It can randomly play out a playable card if there is any. Otherwise, it will draw a card.
 */
public class BaselineAI extends Player {

    /**
     * Play out a playable card if there is any by returning its index. Otherwise return -1 to draw
     * This function will be called by Server.
     */
    public int playCard(Util.Color currColor, int currNumber, Util.Special currSpecial, int penalty
                         ) {
        for (int i = 0; i < cards.size(); i++) {
            if (validate(i, currColor, currNumber, currSpecial, penalty)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Randomly choose a color if a wild/wild draw four is played out.
     * This function will be called by Server.
     */
    public Util.Color chooseColor() {
        Random rand = new Random();
        int random = rand.nextInt() % 4;
        if (random == 0) {
            return Util.Color.RED;
        } else if (random == 1) {
            return Util.Color.BLUE;
        } else if (random == 2) {
            return Util.Color.YELLOW;
        } else {
            return Util.Color.GREEN;
        }
    }

    /**
     * A helper function to check whether a card is playable given the current game state.
     */
    protected boolean validate(int index, Util.Color currColor, int currNumber, Util.Special currSpecial, int penalty) {
        if (penalty != 0) {
            // if there is penalty, check whether the incoming card matches the last card
            if (currSpecial == Util.Special.WILDDRAWFOUR && getCard(index).getSpecial() == Util.Special.WILDDRAWFOUR) {
                return true;
            }
            if (currSpecial == Util.Special.DRAWTWO && getCard(index).getSpecial() == Util.Special.DRAWTWO) {
                return true;
            }
        } else {
            // check whether the incoming card matches one of the game state
            // if it is wild or wild draw four, it is valid
            if (getCard(index).getColor() == currColor) return true;
            if (getCard(index).getNumber() == currNumber && currNumber != NONE_NUMBER) return true;
            if (getCard(index).getSpecial() == currSpecial && currSpecial != Util.Special.NONE) return true;
            if (getCard(index).getSpecial() == Util.Special.WILDDRAWFOUR) return true;
            if (getCard(index).getSpecial() == Util.Special.WILD) return true;
        }
        return false;
    }
}
