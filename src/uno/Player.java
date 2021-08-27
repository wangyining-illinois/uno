package uno;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

public class Player {
    protected ArrayList<Card> cards;
    protected int cardCount;

    /**
     * Initialize a Player Object
     */
    public Player () {
        cards = new ArrayList<Card>();
        cardCount = 0;
    }

    /**
     * Player draws a card by adding the card to the cards List.
     * @param newCard the card drawn by the player
     */
    public void draw (Card newCard) {
        cards.add(newCard);
        cardCount++;
    }

    /**
     * get the number of cards the Player has
     * @return the number of cards
     */
    public int getCardCount() {

        return cardCount;
    }

    /**
     * get the card with a particular index. The index starts at 0.
     * @param cardIndex The index of the card
     * @return the card with a particular index
     */
    public Card getCard(int cardIndex) {

        return cards.get(cardIndex);
    }

    /**
     * remove a card from the Player's hand card
     * This function is called in Server after checking the validity of the card.
     * @param cardIndex The index of the card
     * @return the card that is played out
     */
    public Card playOutCard(int cardIndex) {
        cardCount--;
        return cards.remove(cardIndex);
    }

    /**
     * print the information of all the Player's hand card
     * This function will call printCardInfo() in Card class
     */
    public void printCards () {
        System.out.println("Your cards are:");
        for (int i = 0; i < cardCount; i++) {
            System.out.print(i + 1);
            System.out.print(". ");
            Card curr = cards.get(i);
            curr.printCardInfo();
        }
    }

    public ArrayList<Card> getAllCards() {
        return cards;
    }
}