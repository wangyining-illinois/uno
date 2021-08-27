package unotest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import uno.Card;
import uno.Util.Color;
import uno.Util.Special;

/**
 * This class tests all basic functionalities of the methods in Card class.
 */
public class CardTest {
    @Test
    public void testCreateNormalCard() {
        Card card = Card.createNormalCard(Color.GREEN, 1);
        assertEquals(Color.GREEN, card.getColor());
        assertEquals(1, card.getNumber());
        assertEquals(Special.NONE, card.getSpecial());
    }

    @Test
    public void testCreateSpecial() {
        Card card = Card.createSpecialCard(Color.RED, Special.DRAWTWO);
        assertEquals(Color.RED, card.getColor());
        assertEquals(-1, card.getNumber());
        assertEquals(Special.DRAWTWO, card.getSpecial());
    }

    @Test
    public void testPrintNormal() {
        Card card = Card.createNormalCard(Color.GREEN, 1);
        card.printCardInfo();
    }

    @Test
    public void testPrintSpecial() {
        Card card = Card.createSpecialCard(Color.RED, Special.DRAWTWO);
        card.printCardInfo();
        card = Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR);
        card.printCardInfo();
    }
}