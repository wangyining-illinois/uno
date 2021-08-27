package unogui;

import java.util.ArrayList;
import uno.Card;
import uno.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * This class represents the view in MVC model. The Controller class will communicate with this class.
 * GUI is a JFrame object that uses a container and a CardLayout to toggle between
 * three JPanels: StartPanel, GamePanel and WinnerPanel. They represent three scenes in view.
 */
public class GUI extends JFrame {
    private CardLayout cards;
    private JPanel container;
    private StartPanel numPlayerPanel;
    private GamePanel gamePanel;
    private WinnerPanel winnerPanel;

    /**
     * The constructor initialize JFrame, container and cards.
     */
    public GUI() {
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cards = new CardLayout();
        container = new JPanel();
        container.setLayout(cards);
        this.add(container);
        this.setVisible(true);
    }

    /**
     * initialize the start scene by adding StartPanel to the container.
     */
    public void initStartScene() {
        numPlayerPanel = new StartPanel();
        container.add(numPlayerPanel, "start");
        cards.show(container, "start");
        this.setVisible(true);
    }

    /**
     * initialize the game scene by adding GamePanel to the container.
     */
    public void initGameScene() {
        gamePanel = new GamePanel();
        container.add(gamePanel, "game");
        cards.show(container, "game");
        this.setVisible(true);
    }

    /**
     * initialize the winner scene by adding WinnerPanel to the container.
     */
    public void initWinnerScene(int winner) {
        winnerPanel = new WinnerPanel(winner);
        container.add(winnerPanel, "end");
        cards.show(container, "end");
        this.setVisible(true);
    }

    /**
     * update GUI according to the current game state, current player
     * updateHandCard() will initialize a button for every card of the current player
     */
    public void updateGamePanel(Util.Color currColor, int currNumber, Util.Special currSpecial, int currPlayer,int penalty,
                                int drawPileNum, int discardPileNum, ArrayList<Card> handCards, ActionListener cardButtonListener) {
        gamePanel.updateColorLabel(currColor);
        gamePanel.updateNumberLabel(currNumber);
        gamePanel.updateSpecialLabel(currSpecial);
        gamePanel.updatePenaltyLabel(penalty);
        gamePanel.updateCurrPlayerLabel(currPlayer);
        gamePanel.updatePileNum(drawPileNum, discardPileNum);
        gamePanel.updateHandCard(handCards, cardButtonListener);
    }


    /**
     * This function will be called by the controller to add action listen to play button
     */
    public void addPlayButtonListener(ActionListener action) {
        numPlayerPanel.addPlayListener(action);
    }

    /**
     * This function will be called by the controller to add action listen to draw button
     */
    public void addDrawButtonListener(ActionListener action) { gamePanel.addDrawListener(action); }

    /**
     * This function will be called by the controller to add action listen to set color buttons
     */
    public void addSetColorButtonListener(ActionListener action) { gamePanel.addSetColorListener(action); }

    public void invalidCardError() {
        gamePanel.flashErrorMessage();
    }

    public void hideInvalidCardError() {
        gamePanel.hideErrorMessage();
    }

    public void setColor() {
        gamePanel.enableSetColor();
    }

    public void hideSetColor() {
        gamePanel.disableSetColor();
    }

    public int[] getNumPlayer() {
        return numPlayerPanel.getNumPlayer();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }




}
