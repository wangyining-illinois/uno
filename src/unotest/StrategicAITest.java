package unotest;
import org.junit.Test;
import uno.Card;
import uno.StrategicAI;
import uno.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class StrategicAITest {
    @Test public void testNormalCard() {
        StrategicAI AI = new StrategicAI();
        Card wild = Card.createSpecialCard(Util.Color.NONE, Util.Special.WILDDRAWFOUR);
        Card skip = Card.createSpecialCard(Util.Color.GREEN, Util.Special.SKIP);
        Card two = Card.createNormalCard(Util.Color.GREEN, 2);
        AI.draw(wild);
        AI.draw(skip);
        AI.draw(two);
        assertEquals(2, AI.playCard(Util.Color.GREEN, 3, Util.Special.NONE, 0));
        assertEquals(1, AI.playCard(Util.Color.RED, -1, Util.Special.SKIP, 0));
        assertEquals(-1, AI.playCard(Util.Color.GREEN, -1, Util.Special.DRAWTWO, 2));
        assertEquals(0, AI.playCard(Util.Color.GREEN, -1, Util.Special.WILDDRAWFOUR, 4));
    }

    @Test public void chooseColor() {
        StrategicAI AI = new StrategicAI();
        Card green = Card.createNormalCard(Util.Color.GREEN, 2);
        Card red = Card.createNormalCard(Util.Color.RED, 1);
        Card blue = Card.createNormalCard(Util.Color.BLUE, 1);
        Card yellow = Card.createNormalCard(Util.Color.YELLOW, 1);
        AI.draw(green);
        assertEquals(Util.Color.GREEN, AI.chooseColor());
        AI.draw(red);
        AI.draw(red);
        assertEquals(Util.Color.RED, AI.chooseColor());
        AI.draw(blue);
        AI.draw(blue);
        AI.draw(blue);
        assertEquals(Util.Color.BLUE, AI.chooseColor());
        AI.draw(yellow);
        AI.draw(yellow);
        AI.draw(yellow);
        AI.draw(yellow);
        assertEquals(Util.Color.YELLOW, AI.chooseColor());
    }

}

