package unotest;

import org.junit.Test;

import uno.Card;
import uno.Util.Direction;
import uno.Util.Color;
import uno.Util.Special;
import uno.Server;

import java.io.InputStream;

import static org.junit.Assert.*;
import static uno.Util.NONE_NUMBER;

/**
 * This class tests all basic functionalities of the methods in Server class.
 */
public class ServerTest {

    @Test
    public void serverInitializationTest() {
        int newPlayerCount = 3;
        Server server = new Server(newPlayerCount, 0);
        assertEquals(newPlayerCount, server.getPlayerCount());
        assertNotEquals(Color.NONE, server.getCurrColor());
        assertNotEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.NONE, server.getCurrSpecial());
        assertEquals(0, server.getNextDraw());
    }

    @Test
    public void playerGetSevenCardsTest() {
        int newPlayerCount = 1;
        Server server = new Server(newPlayerCount, 0);
        assertEquals(7, server.getPlayerByIndex(0).getCardCount());
    }


    @Test
    public void checkIfWinTest() {
        Server server = new Server(2, 0);
        for (int i = 0; i < 7; i++) {
            server.getPlayerByIndex(0).playOutCard(0);
        }
        assertEquals(true, server.checkIfWin());
        assertEquals(0, server.getWinner());
    }

    @Test
    public void updateCurrentPlayerTest() {
        Server server = new Server(3, 0);
        if (server.getDirection() == Direction.COUNTER_CLOCKWISE) server.changeDirection();
        server.updateCurrentPlayer();
        assertEquals(1, server.getCurrentPlayer());
        server.changeDirection();
        server.updateCurrentPlayer();
        server.updateCurrentPlayer();
        assertEquals(2, server.getCurrentPlayer());
    }

    @Test public void parseNormalCardTest() {
        Server server = new Server(3, 0);
        Card normalCard = Card.createNormalCard(Color.GREEN, 1);
        server.parseNewCard(normalCard);
        assertEquals(Color.GREEN, server.getCurrColor());
        assertEquals(1, server.getCurrNumber());
        assertEquals(Special.NONE, server.getCurrSpecial());
    }

    /**
     * test whether parseNewCard will move to the next player and update the game state.
     */
    @Test public void parseSkipTest() {
        Server server = new Server(3, 0);
        if (server.getDirection() == Direction.COUNTER_CLOCKWISE) server.changeDirection();
        Card skipCard = Card.createSpecialCard(Color.YELLOW, Special.SKIP);
        server.parseNewCard(skipCard);
        assertEquals(1, server.getCurrentPlayer());
        assertEquals(Color.YELLOW, server.getCurrColor());
        assertEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.SKIP, server.getCurrSpecial());
    }

    @Test
    public void parseWildTest() {
        Server server = new Server(3, 0);
        Card wildCard = Card.createSpecialCard(Color.NONE, Special.WILD);
        server.parseNewCard(wildCard);
        server.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, server.getCurrColor());
        assertEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.WILD, server.getCurrSpecial());
    }

    @Test
    public void parseReverseTest() {
        Server server = new Server(3, 0);
        if (server.getDirection() == Direction.COUNTER_CLOCKWISE) server.changeDirection();;
        Card reverseCard = Card.createSpecialCard(Color.BLUE, Special.REVERSE);
        server.parseNewCard(reverseCard);
        assertEquals(Color.BLUE, server.getCurrColor());
        assertEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.REVERSE, server.getCurrSpecial());
        assertEquals(Direction.COUNTER_CLOCKWISE, server.getDirection());
    }

    /**
     * test that parseNewCard can increase nextdraw by 2 and that nextdraw can be stacked
     */
    @Test public void parseDrawTwoTest() {
        Server server = new Server(3, 0);
        Card drawTwoCard = Card.createSpecialCard(Color.BLUE, Special.DRAWTWO);
        server.parseNewCard(drawTwoCard);
        assertEquals(Color.BLUE, server.getCurrColor());
        assertEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.DRAWTWO, server.getCurrSpecial());
        assertEquals(2, server.getNextDraw());

        server.parseNewCard(drawTwoCard);
        assertEquals(4, server.getNextDraw());
    }

    @Test public void parseWildDrawFourTest() {
        Server server = new Server(3, 0);
        Card wildDrawFourCard = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        server.parseNewCard(wildDrawFourCard);
        server.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, server.getCurrColor());
        assertEquals(NONE_NUMBER, server.getCurrNumber());
        assertEquals(Special.WILDDRAWFOUR, server.getCurrSpecial());
        assertEquals(4, server.getNextDraw());
    }

    /**
     * test current player will be penalized if last player played out draw two or wild draw four
     */
    @Test public void shouldBePenalizedTest() {
        Server server = new Server(3, 0);
        Card drawTwoCard = Card.createSpecialCard(Color.BLUE, Special.DRAWTWO);
        server.parseNewCard(drawTwoCard);
        Card normalCard = Card.createNormalCard(Color.GREEN, 1);
        boolean result = server.shouldBePenalized(normalCard);
        assertEquals(true, result);
    }

    /**
     * test current player can avoid penalty if the card of the same type can be played out
     */
    @Test public void shouldNotBePenalizedTest() {
        Server server = new Server(3, 0);
        Card drawTwoCard = Card.createSpecialCard(Color.BLUE, Special.DRAWTWO);
        server.parseNewCard(drawTwoCard);
        Card anotherDrawTwoCard = Card.createSpecialCard(Color.GREEN, Special.DRAWTWO);
        boolean result = server.shouldBePenalized(anotherDrawTwoCard);
        assertEquals(false, result);
    }

    /**
     * test the current player should still be penalized if a draw card of different type is played out
     * If the last card is draw two and current player has a wild draw four, he should still be penalized, vise versa
     */
    @Test public void shouldBePenalizedTest2() {
        Server server = new Server(3, 0);
        Card drawTwoCard = Card.createSpecialCard(Color.BLUE, Special.DRAWTWO);
        server.parseNewCard(drawTwoCard);
        Card wildDrawFourCard = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        boolean result = server.shouldBePenalized(wildDrawFourCard);
        assertEquals(true, result);
    }

    /**
     * test if cards of different color is not valid and cards of the same color is valid
     */
    @Test public void diffColorValidationTest() {
        Server server = new Server(3, 0);
        Card blueSpecialCard = Card.createSpecialCard(Color.BLUE, Special.SKIP);
        server.parseNewCard(blueSpecialCard);
        Card blueNormalCard = Card.createNormalCard(Color.BLUE, 2);
        assertEquals(true, server.validCard(blueNormalCard));

        Card redNormalCard = Card.createNormalCard(Color.RED, 3);
        assertEquals(false,server.validCard(redNormalCard));
    }

    @Test public void diffNumberValidationTest() {
        Server server = new Server(3, 0);
        Card oneNormalCard = Card.createNormalCard(Color.BLUE, 1);
        server.parseNewCard(oneNormalCard);
        Card twoNormalCard = Card.createNormalCard(Color.RED, 2);
        assertEquals(false, server.validCard(twoNormalCard));

        Card anotherNormalCard = Card.createNormalCard(Color.RED, 1);
        assertEquals(true,server.validCard(anotherNormalCard));
    }

    @Test public void diffSpecialValidationTest() {
        Server server = new Server(3, 0);
        Card skipCard = Card.createSpecialCard(Color.BLUE, Special.SKIP);
        server.parseNewCard(skipCard);
        Card drawTwoCard = Card.createSpecialCard(Color.RED, Special.DRAWTWO);
        assertEquals(false, server.validCard(drawTwoCard));

        Card skipCard2 = Card.createSpecialCard(Color.RED, Special.SKIP);
        assertEquals(true,server.validCard(skipCard2));
    }

    /**
     * test whether wild and wild draw four are valid to any current card
     */
    @Test public void wildValidationTest() {
        Server server = new Server(3, 0);
        Card skipCard = Card.createSpecialCard(Color.BLUE, Special.SKIP);
        server.parseNewCard(skipCard);
        Card wildCard = Card.createSpecialCard(Color.NONE, Special.WILD);
        assertEquals(true, server.validCard(wildCard));

        Card wildDrawFourCard = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        assertEquals(true,server.validCard(wildDrawFourCard));
    }

    @Test public void additionRuleTest() {
        Server server = new Server(3, 0);
        Card three = Card.createNormalCard(Color.GREEN, 3);
        Card four = Card.createNormalCard(Color.YELLOW, 4);
        Card seven = Card.createNormalCard(Color.BLUE, 7);

        //last card is seven
        server.parseNewCard(seven);
        assertTrue(server.validateTwoCards(three, four));

        //play out three and four
        server.parseTwoCards(three, four);
        assertEquals(4, server.getCurrNumber());
        assertEquals(Color.YELLOW, server.getCurrColor());
    }

    @Test public void subtractionRuleTest() {
        Server server = new Server(3, 0);
        Card three = Card.createNormalCard(Color.GREEN, 3);
        Card four = Card.createNormalCard(Color.YELLOW, 4);
        Card seven = Card.createNormalCard(Color.BLUE, 7);
        Card eight = Card.createNormalCard(Color.RED, 8);

        //last card is three
        server.parseNewCard(three);
        assertTrue(server.validateTwoCards(seven, four));
        assertTrue(server.validateTwoCards(four, seven));
        assertFalse(server.validateTwoCards(seven, eight));

        //play out seven and four
        server.parseTwoCards(seven, four);
        assertEquals(4, server.getCurrNumber());
        assertEquals(Color.YELLOW, server.getCurrColor());
    }

    @Test public void stackValidationTest() {
        Server server = new Server(3, 0);
        Card drawTwo = Card.createSpecialCard(Color.GREEN, Special.DRAWTWO);
        server.parseNewCard(drawTwo);
        assertTrue(server.validCard(drawTwo));
        Card wildfour = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        server.parseNewCard(wildfour);
        assertTrue(server.validCard(wildfour));
    }

    @Test public void playersTurnDrawTest() {
        Server server =  new Server(2, 0);
        server.playersTurn(-1);
        assertEquals(8, server.getPlayerByIndex(0).getCardCount());
    }

    @Test public void playersTurnPenaltyTest() {
        Server server = new Server(2, 0);
        Card wildfour = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        server.parseNewCard(wildfour);
        server.playersTurn(-1);
        assertEquals(11, server.getPlayerByIndex(0).getCardCount());
    }

    @Test public void playersTurnPenalty() {
        Server server = new Server(2, 0);
        Card wildfour = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        server.parseNewCard(wildfour);

        Card card = Card.createNormalCard(Color.BLUE, 2);
        server.getPlayerByIndex(0).draw(card);
        server.playersTurn(7);
        assertEquals(12, server.getPlayerByIndex(0).getCardCount());

        server.parseNewCard(card);
        server.playersTurn(7);
    }

    @Test public void validByIndexTest() {
        Server server = new Server(2, 0);
        Card card = Card.createNormalCard(Color.BLUE, 2);
        server.parseNewCard(card);
        server.getPlayerByIndex(0).draw(card);
        assertTrue(server.validCardByIndex(7));
    }
}
