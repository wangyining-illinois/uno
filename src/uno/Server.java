package uno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uno.Util.Color;
import uno.Util.Special;
import uno.Util.Direction;

import static uno.Util.NONE_NUMBER;

public class Server {

    private List<Player> players;
    private Pile pile;
    private int playerCount;
    private int currentPlayer;
    private int AICount;
    private int humanCount;

    private Color currColor;
    private int currNumber;
    private Special currSpecial;

    private int nextdraw;
    private Direction direction;
    private boolean skip;
    private int winner;

    /**
     * Initialize a Server Object with newPlayerCount number of players and newAICount number of AIs
     * @param newPlayerCount number of players in the game
     */
    public Server(int newPlayerCount, int newAICount) {
        AICount = newAICount;
        humanCount = newPlayerCount;
        playerCount = newPlayerCount + newAICount;
        currentPlayer = 0;
        Random rand = new Random();
        int rand_int = rand.nextInt();
        if (rand_int % 2 == 0) {
            direction = Direction.CLOCKWISE;
        } else {
            direction = Direction.COUNTER_CLOCKWISE;
        }
        currColor = Color.NONE;
        currNumber = NONE_NUMBER;
        currSpecial = Special.NONE;
        nextdraw = 0;
        skip = false;
        winner = -1;

        pile = new Pile();
        players = new ArrayList<Player>(playerCount);
        for (int i = 0; i < playerCount; i++) {
            if (i < humanCount) {
                players.add(new Player());
            } else {
                players.add(new StrategicAI());
            }
            for (int j = 0; j < 7; j++) {
                players.get(i).draw(pile.drawFromPile());
            }
        }
        Card topCard = pile.drawFromPile();
        while (topCard.getSpecial() != Special.NONE) {
            pile.discard(topCard);
            pile.discardAll();
            topCard = pile.drawFromPile();
        }
        currColor = topCard.getColor();
        currNumber = topCard.getNumber();
        pile.discard(topCard);
    }


    /**
     * This method checks if the current player wins after the player's turn
      */
    public boolean checkIfWin() {
        if (players.get(currentPlayer).getCardCount() == 0) {
            winner = currentPlayer;
            return true;
        }
        return false;
    }

    /**
     * update the current player according to the current direction of the game
     * If the next player is AI, handle its turn.
     */
    public void updateCurrentPlayer() {
        if (direction == Direction.CLOCKWISE) currentPlayer = (currentPlayer + 1) % playerCount;
        else{
            currentPlayer--;
            if (currentPlayer < 0) currentPlayer += playerCount;
        }
        if (currentPlayer >= humanCount) AIsTurn();
    }

    /**
     * Simulate an AI's turn by asking StrategicAI class to play card according to the current state, and
     * change color if wild/wild draw four is played.
     */
    private void AIsTurn() {
        StrategicAI AI = (StrategicAI)players.get(currentPlayer);
        int index = AI.playCard(currColor, currNumber, currSpecial, nextdraw);
        if (index == -1) {
            // The AI wants to draw, first check if there is penalty
            if (nextdraw != 0) {
                playersTurn(-1);
                return;
            }
            // If there is no penalty, draw a card and play it if possible.
            Card newCard = pile.drawFromPile();
            AI.draw(newCard);
            if (validCard(newCard)) {
                // handle set color
                if (newCard.getSpecial() == Special.WILD || newCard.getSpecial() == Special.WILDDRAWFOUR) {
                    setColor(AI.chooseColor());
                }
                playersTurn(AI.getCardCount() - 1);
            }
        } else {
            // if AI wants to play out a card, first handle set color
            if (AI.getCard(index).getSpecial() == Special.WILD || AI.getCard(index).getSpecial() == Special.WILDDRAWFOUR) {
                setColor(AI.chooseColor());
            }
            playersTurn(index);
        }
        updateCurrentPlayer();
    }

    /**
     * This method simulates a player's round by getting index of the intend card, checking penalty,
     * applying the penalty, or playing out the card.
     * Update: this function will be called by the controller instead of the infinite loop in Server
     */
    public void playersTurn(int cardIndex) {
        if (cardIndex == -1) {
            // The player intends to draw a card, check if there is penalty.
            if (nextdraw != 0) {
                for (int i = 0; i < nextdraw; i++) {
                    players.get(currentPlayer).draw(pile.drawFromPile());
                }
                nextdraw = 0;
            } else {
                //Update: when there is no penalty and player wants to draw a card, simply draw a card.
                // Whether this card is playable will be handled by the controller
                Card newcard = pile.drawFromPile();
                players.get(currentPlayer).draw(newcard);

            }
        } else {
            // The player intends to play out a valid card, but first check if there is penalty
            if (nextdraw != 0 && shouldBePenalized(players.get(currentPlayer).getCard(cardIndex))) {
                for (int i = 0; i < nextdraw; i++) {
                    players.get(currentPlayer).draw(pile.drawFromPile());
                }
                nextdraw = 0;
            } else {
                // no penalty, play out the card and move it to discard pile
                Card card = players.get(currentPlayer).playOutCard(cardIndex);
                pile.discard(card);
                parseNewCard(card);
            }
        }
        return;
    }


    /**
     * This function is called to validate when two cards are played out.
     * The two cards are valid iff the addition or subtraction of the two cards are equal to the current number.
     * @param card1 first card played out.
     * @param card2 second card played out. It will be the top card on the discard pile.
     * @return whether the two cards are valid
     */
    public boolean validateTwoCards(Card card1, Card card2) {
        if (card1.getNumber() == -1 || card2.getNumber() == -1) return false;
        if (card1.getNumber() + card2.getNumber() == currNumber) {
            return true;
        } else if (card1.getNumber() - card2.getNumber() == currNumber && currNumber != -1){
            return true;
        } else if (card2.getNumber() - card1.getNumber() == currNumber && currNumber != -1){
            return true;
        }
        return false;
    }

    /**
     * This function is called after validation of the two cards. The game state will be changed according to card2.
     * @param card1 first card being played out.
     * @param card2 second card being played out. It will be the top card on the discard pile.
     */
    public void parseTwoCards(Card card1, Card card2) {
        currColor = card2.getColor();
        currNumber = card2.getNumber();
        currSpecial = Special.NONE;
    }


    /**
     *  This method parses the new card played out by a player and modifies the game state.
     */
    public void parseNewCard(Card card) {
        if (card.getSpecial() != Special.NONE) {
            if (card.getSpecial() == Special.WILD) {
                //currColor = readNewColor();
                currNumber = NONE_NUMBER;
                currSpecial = Special.WILD;
            } else if (card.getSpecial() == Special.WILDDRAWFOUR){
                //currColor = readNewColor();
                currNumber = NONE_NUMBER;
                currSpecial = Special.WILDDRAWFOUR;
                nextdraw += 4;
            } else if (card.getSpecial() == Special.REVERSE) {
                currColor = card.getColor();
                currNumber = NONE_NUMBER;
                currSpecial = Special.REVERSE;
                changeDirection();
            } else if (card.getSpecial() == Special.SKIP) {
                currColor = card.getColor();
                currNumber = NONE_NUMBER;
                currSpecial = Special.SKIP;
                if (direction == Direction.CLOCKWISE) currentPlayer = (currentPlayer + 1 )% playerCount;
                else {
                    currentPlayer--;
                    if (currentPlayer < 0) currentPlayer += playerCount;
                }
            } else if (card.getSpecial() == Special.DRAWTWO) {
                currColor = card.getColor();
                currNumber = NONE_NUMBER;
                currSpecial = Special.DRAWTWO;
                nextdraw += 2;
            }
        } else {
            currColor = card.getColor();
            currNumber = card.getNumber();
            currSpecial = Special.NONE;
        }
    }

    /**
     *This method tests whether the card that the current player intends to play out is valid, according to the last
     * card that was played out.
     */
    public boolean validCard(Card card) {
        if (nextdraw != 0) {
            // if there is penalty, check whether the incoming card matches the last card
            if (currSpecial == Special.WILDDRAWFOUR && card.getSpecial() == Special.WILDDRAWFOUR) {
                return true;
            }
            if (currSpecial == Special.DRAWTWO && card.getSpecial() == Special.DRAWTWO) {
                return true;
            }
        } else {
            // check whether the incoming card matches one of the game state
            // if it is wild or wild draw four, it is valid
            if (card.getColor() == currColor) return true;
            if (card.getNumber() == currNumber && currNumber != NONE_NUMBER) return true;
            if (card.getSpecial() == currSpecial && currSpecial != Special.NONE) return true;
            if (card.getSpecial() == Special.WILDDRAWFOUR) return true;
            if (card.getSpecial() == Special.WILD) return true;
        }
        return false;
    }

    /**
     * Validate a card by its index in the current player's handcard.
     * This function will be called by the controller to validate a card
     * @param index The index of the card being validated
     * @return whether the card is valid
     */
    public boolean validCardByIndex(int index) {
        if (index == -1) return true;
        Card card = players.get(currentPlayer).getCard(index);
        return validCard(card);
    }

    /**
     * This method will test whether the current player should be penalized.
     */
    public boolean shouldBePenalized (Card currCard) {
        if (currCard.getSpecial() == Special.DRAWTWO && currSpecial == Special.DRAWTWO) {
            return false;
        } else if (currCard.getSpecial() == Special.WILDDRAWFOUR && currSpecial == Special.WILDDRAWFOUR) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * change the direction of the game. This method will be called when a reverse card is played
     */
    public void changeDirection() {
        if (direction == Direction.CLOCKWISE) direction = Direction.COUNTER_CLOCKWISE;
        else direction = Direction.CLOCKWISE;
    }

    /**
     * Set the current server. This function will be called by the controller after it receive the color from GUI
     * @param color the new color received from the buttons in GUI
     */
    public void setColor(Color color) {
        currColor = color;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public Color getCurrColor() {
        return currColor;
    }

    public int getCurrNumber() {
        return currNumber;
    }

    public Special getCurrSpecial() {
        return currSpecial;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getWinner() {
        return winner;
    }

    public Player getPlayerByIndex(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerCount) return null;
        return players.get(playerIndex);
    }

    public int getNextDraw(){
        return nextdraw;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDrawPileNum() {
        return pile.getDrawPileNum();
    }

    public int getDiscardPileNum() {
        return pile.getDiscardPileNum();
    }

    public ArrayList<Card> getHandCards() {
        return players.get(currentPlayer).getAllCards();
    }
}