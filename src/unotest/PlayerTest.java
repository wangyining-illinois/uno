package unotest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import uno.Card;
import uno.Player;
import uno.Util.Color;
import uno.Util.Special;


/**
 * This class tests all basic functionalities of the methods in Player class.
 */
public class PlayerTest {
    @Test
    public void cardNumIncreaseTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        assertEquals(1, player.getCardCount());
    }

    @Test
    public void cardNumDecreaseTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        assertEquals(1, player.getCardCount());
        player.playOutCard(0);
        assertEquals(0, player.getCardCount());
    }

    @Test
    public void addCorrectCardTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        assertEquals(Color.GREEN, player.getCard(0).getColor());
        assertEquals(1, player.getCard(0).getNumber());
        assertEquals(Special.NONE, player.getCard(0).getSpecial());
    }

    @Test
    public void removeCorrectCardTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        player.draw(Card.createSpecialCard(Color.NONE, Special.WILD));
        player.playOutCard(0);
        assertEquals(Color.NONE, player.getCard(0).getColor());
        assertEquals(-1, player.getCard(0).getNumber());
        assertEquals(Special.WILD, player.getCard(0).getSpecial());
    }

    @Test
    public void printCardsTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        player.draw(Card.createSpecialCard(Color.NONE, Special.WILD));
        player.draw(Card.createSpecialCard(Color.YELLOW, Special.DRAWTWO));
        player.printCards();
    }

    @Test public void getCardsTest() {
        Player player = new Player();
        player.draw(Card.createNormalCard(Color.GREEN, 1));
        assertEquals(Color.GREEN, player.getAllCards().get(0).getColor());
        assertEquals(1, player.getAllCards().get(0).getNumber());
    }
}
