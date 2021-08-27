package uno;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import uno.Util.Color;
import uno.Util.Special;

import static uno.Util.INITIAL_PILE_CAPACITY;

/**
 * A Pile Object represents the draw pile and the discard pile in a UNO game.
 */
public class Pile {
    private List<Card> drawPile;
    private int drawNum;
    private List<Card> discardPile;
    private int discardNum;

    /**
     * The constructor initiates the draw pile and the discard pile.
     * The draw pile will be filled with 108 cards that have been shuffled.
     */
    public Pile () {
        drawPile = new ArrayList<Card>(INITIAL_PILE_CAPACITY);
        discardPile = new ArrayList<Card>(INITIAL_PILE_CAPACITY);
        drawNum = INITIAL_PILE_CAPACITY;
        discardNum = 0;
        for (int i = 0; i < 10; i++) {
            for (Color color: Color.values()) {
                if (color == Color.NONE) continue;
                drawPile.add(Card.createNormalCard(color, i));
            }
            if (i != 0) {
                for (Color color: Color.values()) {
                    if (color == Color.NONE) continue;
                    drawPile.add(Card.createNormalCard(color, i));
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (Color color: Color.values()) {
                if (color == Color.NONE) continue;
                drawPile.add(Card.createSpecialCard(color, Special.SKIP));
                drawPile.add(Card.createSpecialCard(color, Special.REVERSE));
                drawPile.add(Card.createSpecialCard(color, Special.DRAWTWO));
            }
        }
        for (int i = 0; i < 4; i++) {
            drawPile.add(Card.createSpecialCard(Color.NONE, Special.WILD));
            drawPile.add(Card.createSpecialCard(Color.NONE, Special.WILDDRAWFOUR));
        }
        shuffle (drawPile, INITIAL_PILE_CAPACITY);
    }

    /**
     * remove the top Card from the draw pile
     * @return the top Card
     */
    public Card drawFromPile () {
        if (drawNum == 0) {
            createNewPile();
        }
        drawNum--;
        return drawPile.get(drawNum);
    }

    /**
     * add a card to the discard pile
     * Player will call this function to play out a hand card
     * @param card The card that a Player would like to play out.
     */
    public void discard (Card card) {
        discardPile.add(card);
        discardNum++;
    }

    /**
     * A helper function used by the Server to discard all cards in the draw pile.
     */
    public void discardAll() {
        discardPile.addAll (drawPile);
        discardNum += drawNum;
        drawNum = 0;
    }

    /**
     * This method is used to shuffle the cards in pile. The number of cards that needs to shuffle is specified by size.
     */
    private void shuffle (List<Card> pile, int size) {
        Random rand = new Random();
        int rand_int;
        for (int i = 0; i < size; i++) {
            rand_int = rand.nextInt(size);
            Collections.swap(pile, i, rand_int);
        }
    }

    /**
     * This method creates a new pile out of the discard pile.
     * This method will be called whenever one player tries to draw from an empty draw pile.
     */
    private void createNewPile () {
        drawPile.removeAll(drawPile);
        shuffle(discardPile, discardNum - 1);
        drawPile.addAll(discardPile);
        discardPile.removeAll(discardPile);
        drawNum = discardNum;
        discardNum = 0;
    }

    public int getDrawPileNum() {
        return drawNum;
    }

    public int getDiscardPileNum() {
        return discardNum;
    }



}