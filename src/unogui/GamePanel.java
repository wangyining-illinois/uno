package unogui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import uno.Card;
import uno.Player;
import uno.Util.Color;
import uno.Util.Special;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * This class is responsible for the game scene.
 * Update: turned this class from a JFrame to a JPanel. The GamePanel object will be constructed by GUI class
 */
public class GamePanel extends JPanel {
    private JLabel colorLabel;
    private JLabel colorLabel2;
    private JLabel numberLabel;
    private JLabel specialLabel;
    private JLabel currPlayerLabel;
    private JLabel penaltyLabel;

    private JLabel drawPileLabel;
    private JLabel discardPileLabel;

    private JLabel setColorTextLabel;
    private JButton greenButton;
    private JButton yellowButton;
    private JButton redButton;
    private JButton blueButton;

    private JButton drawButton;
    private JButton hideButton;
    private boolean isHidden;

    // an array list of jbuttons for every card
    private ArrayList<JButton> cardButtons = new ArrayList<JButton>();

    private JLabel errorMessage;

    /**
     * The constructor will initialize the game scene.
     */
    public GamePanel() {
        this.setLayout(null);

        initializeStateLabel();

        initializePileLabel();

        initializeSetColorButton();

        initializeDrawButton();
        initializeHideButton();

        this.setVisible(true);
    }

    /**
     * This function will initialize the hide/reveal button and add action listen to it.
     * The action listener will hide or reveal the hand cards.
     */
    private void initializeHideButton() {
        isHidden = false;
        hideButton = new JButton("Hide/Reveal");
        hideButton.setBounds(800, 425, 100, 25);
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isHidden) {
                    for (JButton button: cardButtons) {
                        button.setVisible(true);
                    }
                    isHidden = false;
                } else {
                    for (JButton button: cardButtons) {
                        button.setVisible(false);
                    }
                    isHidden = true;
                }
            }
        });
        this.add(hideButton);
    }

    private void initializeDrawButton() {
        drawButton = new JButton("Draw");
        drawButton.setBounds(800, 400, 100, 25);
        this.add(drawButton);
    }

    /**
     * This method renders the set color panel with four buttons.
     */
    private void initializeSetColorButton() {

        setColorTextLabel = new JLabel("Set New Color");
        setColorTextLabel.setBounds(250, 250,150, 25);
        setColorTextLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(setColorTextLabel);

        greenButton = new JButton();
        greenButton.setBackground(java.awt.Color.green);
        greenButton.setBounds(400, 250, 60, 25);
        this.add(greenButton);

        redButton = new JButton();
        redButton.setBackground(java.awt.Color.red);
        redButton.setBounds(460, 250, 60, 25);
        this.add(redButton);

        yellowButton = new JButton();
        yellowButton.setBackground(java.awt.Color.yellow);
        yellowButton.setBounds(520, 250, 60, 25);
        this.add(yellowButton);

        blueButton = new JButton();
        blueButton.setBackground(java.awt.Color.blue);
        blueButton.setBounds(580, 250, 60, 25);
        this.add(blueButton);

        setColorTextLabel.setVisible(false);
        greenButton.setVisible(false);
        blueButton.setVisible(false);
        redButton.setVisible(false);
        yellowButton.setVisible(false);
    }

    /**
     * This method initialize two labels representing the draw pile and discard pile.
     * The number of cards in both piles will be displayed.
     */
    private void initializePileLabel() {
        drawPileLabel = new JLabel("", SwingConstants.CENTER);
        drawPileLabel.setBounds(200, 100, 200, 100);
        Border blackline = BorderFactory.createLineBorder(java.awt.Color.black);
        drawPileLabel.setBorder(blackline);
        this.add(drawPileLabel);

        discardPileLabel = new JLabel("", SwingConstants.CENTER);
        discardPileLabel.setBounds(500, 100, 200, 100);
        discardPileLabel.setBorder(blackline);
        this.add(discardPileLabel);
    }

    /**
     * This function initialize labels for the game state (i.e. color, number, special type, penalty, curr player).
     * Update: it will initialize error message label
     */
    private void initializeStateLabel() {
        colorLabel = new JLabel("Color: ");
        colorLabel.setBounds(200, 20, 100, 25);
        this.add(colorLabel);

        colorLabel2 = new JLabel();
        colorLabel2.setBounds(250, 20, 25, 25);
        colorLabel2.setOpaque(true);
        //colorLabel2.setBackground(java.awt.Color.black);
        this.add(colorLabel2);

        numberLabel = new JLabel();
        numberLabel.setBounds(300, 20, 200, 25);
        this.add(numberLabel);

        specialLabel = new JLabel();
        specialLabel.setBounds(400, 20, 200, 25);
        this.add(specialLabel);

        penaltyLabel = new JLabel();
        penaltyLabel.setBounds(600, 20, 200, 25);
        this.add(penaltyLabel);

        currPlayerLabel = new JLabel();
        currPlayerLabel.setBounds(0, 300, 200, 50);
        currPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(currPlayerLabel);

        errorMessage = new JLabel("Select a valid card, or draw a new card");
        errorMessage.setBounds(400, 300, 300, 25);
        this.add(errorMessage);
        errorMessage.setVisible(false);
    }

    /**
     * This method is used to update the color label according to the current color.
     */
    public void updateColorLabel(Color currColor) {
        switch (currColor){
            case GREEN:
                colorLabel2.setBackground(java.awt.Color.green);
                break;
            case YELLOW:
                colorLabel2.setBackground(java.awt.Color.yellow);
                break;
            case RED:
                colorLabel2.setBackground(java.awt.Color.red);
                break;
            case BLUE:
                colorLabel2.setBackground(java.awt.Color.blue);
                break;
            case NONE:
                colorLabel2.setBackground(java.awt.Color.black);
                break;
        }
    }

    /**
     * This method is used to update the number label according to the current number.
     */
    public void updateNumberLabel(int currNumber) {
        if (currNumber == -1) {
            numberLabel.setText("Number: ");
        } else {
            numberLabel.setText("Number: " + currNumber);
        }
    }

    /**
     * This method is used to update the special label according to the current special type.
     */
    public void updateSpecialLabel(Special currSpecial) {
        switch (currSpecial){
            case REVERSE:
                specialLabel.setText("Special Type: Reverse");
                break;
            case SKIP:
                specialLabel.setText("Special Type: Skip");
                break;
            case DRAWTWO:
                specialLabel.setText("Special Type: Draw Two");
                break;
            case WILD:
                specialLabel.setText("Special Type: Wild");
                break;
            case WILDDRAWFOUR:
                specialLabel.setText("Special Type: Wild Draw Four");
                break;
            case NONE:
                specialLabel.setText("Special Type: ");
        }
    }

    /**
     * This method is used to update the label that displays the current player.
     */
    public void updateCurrPlayerLabel(int currPlayerIndex) {
        currPlayerLabel.setText("Player " + currPlayerIndex + "'s Turn");
    }

    /**
     * This method is used to update the label that displays the current penalty.
     */
    public void updatePenaltyLabel(int currPenalty) {
        penaltyLabel.setText("Stacked Penalty: " + currPenalty);
    }

    public void updatePileNum(int drawPileNum, int discardPileNum) {
        drawPileLabel.setText("Draw Pile: " + drawPileNum + " cards");
        discardPileLabel.setText("Discard Pile: " + discardPileNum + " cards");
    }

    /**
     * This method is used to render the current player's hand cards.
     * This method will call initializeCardButton() for every card the player has.
     * @param handCards the list of Card that the current player has
     * @param cardButtonListener an action listener to indicate the controller that a button is pressed.
     */
    public void updateHandCard(ArrayList<Card> handCards, ActionListener cardButtonListener) {
        // first remove all card buttons of the last player
        for (int i = 0; i < cardButtons.size(); i++) {
            this.remove(cardButtons.get(i));
        }
        cardButtons = new ArrayList<JButton>();
        for (int i = 0; i < handCards.size(); i++) {
            initializeCardButton(i, handCards.get(i), cardButtonListener);
        }
        this.repaint();
    }

    /**
     * This method renders one of the current player's hand card. It will create a card button on the panel.
     * @param index index of the card. This determines the position of the button
     * @param card one hand card of the current player
     * @param cardButtonListener an action listener to indicate the controller that a button is pressed.
     *                           The position of the card button in cardButtons list will be passed to controller
     */
    private void initializeCardButton(int index, Card card, ActionListener cardButtonListener) {
        // One layer can only contain 8 cards due to the size of the card button
        // If there are too many cards, the buttons will be placed on the next layer.
        int layer = index / 8;

        // cardButtons contains all card buttons of the current player
        cardButtons.add(new JButton());
        cardButtons.get(index).addActionListener(cardButtonListener);
        cardButtons.get(index).setBounds((index % 8) * 100, 350 + layer * 150, 100, 150);

        // set color and text of the card button
        switch (card.getColor()) {
            case GREEN:
                cardButtons.get(index).setBackground(java.awt.Color.green);
                break;
            case BLUE:
                cardButtons.get(index).setBackground(java.awt.Color.blue);
                break;
            case YELLOW:
                cardButtons.get(index).setBackground(java.awt.Color.yellow);
                break;
            case RED:
                cardButtons.get(index).setBackground(java.awt.Color.red);
                break;
        }
        if (card.getNumber() != -1) {
            cardButtons.get(index).setText(String.valueOf(card.getNumber()));
            cardButtons.get(index).setFont(new Font("Arial", Font.PLAIN, 20));
        } else {
            String special = "";
            switch (card.getSpecial()) {
                case REVERSE:
                    special = "<html>REV- ERSE<html>";
                    break;
                case SKIP:
                    special = "SKIP";
                    break;
                case DRAWTWO:
                    special = "<html>DRAW TWO<html>";
                    break;
                case WILD:
                    special = "WILD";
                    break;
                case WILDDRAWFOUR:
                    special = "<html>WILD DRAW FOUR<html>";
            }
            cardButtons.get(index).setText(special);
            cardButtons.get(index).setFont(new Font("Arial", Font.PLAIN, 20));
        }
        this.add(cardButtons.get(index));
    }

    public ArrayList<JButton> getCardButtons() {
        return cardButtons;
    }

    public void flashErrorMessage() {
        errorMessage.setVisible(true);
    }

    public void hideErrorMessage() {
        errorMessage.setVisible(false);
    }

    /**
     * add a listener to Draw button
     * @param action an action listener to indicate the controller that the player wants to draw a card
     */
    public void addDrawListener(ActionListener action) { drawButton.addActionListener(action); }

    /**
     * add a listener to each color button on the set color panel
     * @param action an action listener to indicate the controller that the new color
     */
    public void addSetColorListener(ActionListener action) {
        greenButton.addActionListener(action);
        redButton.addActionListener(action);
        yellowButton.addActionListener(action);
        blueButton.addActionListener(action);
    }

    public void enableSetColor() {
        setColorTextLabel.setVisible(true);
        greenButton.setVisible(true);
        blueButton.setVisible(true);
        redButton.setVisible(true);
        yellowButton.setVisible(true);
    }

    public void disableSetColor() {
        setColorTextLabel.setVisible(false);
        greenButton.setVisible(false);
        blueButton.setVisible(false);
        redButton.setVisible(false);
        yellowButton.setVisible(false);
    }

    public JButton getGreenButton() { return greenButton; }
    public JButton getRedButton() { return redButton; }
    public JButton getYellowButton() { return yellowButton; }
    public JButton getBlueButton() { return blueButton; }


}
