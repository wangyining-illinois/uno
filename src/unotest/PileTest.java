package unotest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static uno.Util.*;

import uno.Card;
import uno.Util.Color;
import uno.Util.Special;
import uno.Pile;

import java.util.HashMap;


/**
 * This class tests all basic functionalities of the methods in Pile class.
 */
public class PileTest {
    // test whether pile constructor initiate a random draw pile with 108 cards correctly
    @Test
    public void testInitiatePile() {
        Pile pile = new Pile();
        HashMap<Color, Integer> colorRemain = new HashMap<Color, Integer>();
        for (Color color: Color.values()) {
            if (color == Color.NONE) colorRemain.put(color, 8);
            else colorRemain.put(color, 25);
        }

        HashMap<Special, Integer> specialRemain = new HashMap<Special, Integer>();
        for (Special special: Special.values()) {
            if (special == Special.WILD || special == Special.WILDDRAWFOUR) specialRemain.put(special, 4);
            else if (special == Special.NONE) specialRemain.put(special, 76);
            else specialRemain.put(special, 8);
        }

        HashMap<Integer, Integer> numberRemain = new HashMap<Integer, Integer>();
        numberRemain.put(NONE_NUMBER, 32);
        numberRemain.put(0, 4);
        for (int i = 1; i < 10; i++) {
            numberRemain.put(i, 8);
        }

        for (int i = 0; i < INITIAL_PILE_CAPACITY; i++) {
            Card card = pile.drawFromPile();
            colorRemain.replace(card.getColor(), colorRemain.get(card.getColor()) - 1);
            numberRemain.replace(card.getNumber(), numberRemain.get(card.getNumber()) - 1);
            specialRemain.replace(card.getSpecial(), specialRemain.get(card.getSpecial()) - 1);
        }

        for (int values: colorRemain.values()) {
            assertEquals(0, values);
        }
        for (int values: numberRemain.values()) {
            assertEquals(0, values);
        }
        for (int values: specialRemain.values()) {
            assertEquals(0, values);
        }
    }

    @Test public void discardTest() {
        Pile pile = new Pile();
        Card card = Card.createNormalCard(Color.BLUE, 1);
        pile.discard(card);
        assertEquals(1, pile.getDiscardPileNum());
    }

    @Test public void replaceTest() {
        Pile pile = new Pile();
        for (int i = 0; i < 108; i++) {
            pile.discard(pile.drawFromPile());
        }
        pile.drawFromPile();
        assertEquals(107, pile.getDrawPileNum());
        pile.discardAll();
        assertEquals(0, pile.getDrawPileNum());
    }

}
