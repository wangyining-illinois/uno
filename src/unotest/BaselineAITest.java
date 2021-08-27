package unotest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import uno.BaselineAI;
import uno.Card;
import uno.Server;
import uno.Util.Color;
import uno.Util.Special;

public class BaselineAITest {
    @Test public void playCardTest() {
        BaselineAI AI = new BaselineAI();
        Card card = Card.createNormalCard(Color.GREEN, 1);
        AI.draw(card);
        assertEquals(0, AI.playCard(Color.GREEN, 2, Special.NONE, 0));
    }

    @Test public void diffColorValidationTest() {
        BaselineAI AI = new BaselineAI();
        Card blueSpecialCard = Card.createSpecialCard(Color.BLUE, Special.SKIP);
        AI.draw(blueSpecialCard);
        assertEquals(0, AI.playCard(Color.BLUE, 0, Special.NONE, 0));
        assertEquals(-1, AI.playCard(Color.RED, 1, Special.NONE, 0));
        assertEquals(-1, AI.playCard(Color.GREEN, -1, Special.WILDDRAWFOUR, 4));
    }

    @Test public void diffNumberValidationTest() {
        BaselineAI AI = new BaselineAI();
        Card blueNormalCard = Card.createNormalCard(Color.BLUE, 2);
        AI.draw(blueNormalCard);
        assertEquals(0, AI.playCard(Color.RED, 2, Special.NONE, 0));
        assertEquals(-1, AI.playCard(Color.RED, 1, Special.NONE, 0));
        assertEquals(-1, AI.playCard(Color.GREEN, -1, Special.WILDDRAWFOUR, 4));
    }

    @Test public void diffSpecialValidationTest() {
        BaselineAI AI = new BaselineAI();
        Card skipCard = Card.createSpecialCard(Color.BLUE, Special.SKIP);
        AI.draw(skipCard);
        assertEquals(0, AI.playCard(Color.RED, -1, Special.SKIP, 0));
        assertEquals(-1, AI.playCard(Color.RED, -1, Special.REVERSE, 0));
        assertEquals(-1, AI.playCard(Color.GREEN, -1, Special.WILDDRAWFOUR, 4));
    }

    /**
     * test whether wild and wild draw four are valid to any current card
     */
    @Test public void wildValidationTest() {
        BaselineAI AI = new BaselineAI();
        Card wildCard = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        AI.draw(wildCard);
        assertEquals(0, AI.playCard(Color.RED, -1, Special.SKIP, 0));
        assertEquals(0, AI.playCard(Color.RED, -1, Special.REVERSE, 0));
        assertEquals(0, AI.playCard(Color.GREEN, -1, Special.WILDDRAWFOUR, 4));
    }

}
