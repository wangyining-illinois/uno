package unocontroller;

import uno.Server;
import uno.Util;
import unogui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller of the MVC structure.
 * Controller will construct a GUI object, which represents the view, and a Server Object, which represents the model.
 */
public class Controller {
    private int numPlayer = -1;
    private int numAI = -1;
    GUI gui;
    Server server;

    /**
     * A constructor to initialize GUI object and the first scene.
     */
    public Controller() {
        gui = new GUI();
        gui.initStartScene();
        handlePlayButtonClick();
    }

    /**
     * Start the game by initializing the game scene in GUI and construct a new server according to player number
     */
    private void startGame() {
        server = new Server(numPlayer, numAI);
        gui.initGameScene();

        //pass action listeners to GUI
        handleDrawButtonClick();
        handleSetColorButtonClick();

        //initialize game panel for the first player
        updateGamePanel();
    }

    /**
     * Pass action listener to the Play Button
     */
    public void handlePlayButtonClick() {
        gui.addPlayButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] numPlayerArray = gui.getNumPlayer();
                numPlayer = numPlayerArray[0];
                numAI = numPlayerArray[1];
                if (numPlayer != -1 && numAI != -1) {
                    // start the game if player number is valid
                    startGame();
                }
            }
        });
    }

    /**
     * pass action listener for draw button to GUI
     */
    public void handleDrawButtonClick() {
        gui.addDrawButtonListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePlayCardByIndex(-1);
            }
        });
    }

    /**
     * pass action listener for set color buttons to GUI
     */
    private void handleSetColorButtonClick() {
        gui.addSetColorButtonListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.hideSetColor();
                if (e.getSource() == gui.getGamePanel().getGreenButton()) {
                    server.setColor(Util.Color.GREEN);
                } else if (e.getSource() == gui.getGamePanel().getBlueButton()) {
                    server.setColor(Util.Color.BLUE);
                } else if (e.getSource() == gui.getGamePanel().getYellowButton()) {
                    server.setColor(Util.Color.YELLOW);
                } else {
                    server.setColor(Util.Color.RED);
                }
                moveToNextPlayer();
            }
        });
    }

    /**
     * Initialize game scene for the next player after the current player finishes
     * If the current player wins after his/her moves, initialize the winner scene
     */
    private void moveToNextPlayer() {
        if (server.checkIfWin()) {
            gui.initWinnerScene(server.getWinner());
        }
        server.updateCurrentPlayer();
        updateGamePanel();
    }

    /**
     * Ask server to play out a card selected by player.
     * @param i The index of the card that the player wants to play. i = -1 if player wants to draw
     */
    public void handlePlayCardByIndex(int i) {
        //first check whether the card is valid according to game state
        if (server.validCardByIndex(i)) {
            gui.hideInvalidCardError();

            // The player wants to draw
            if (i == -1) {
                if (server.getNextDraw() == 0) {
                    // If there is no penalty, draw a card and check whether the new card is playable.
                    server.playersTurn(-1);
                    handleDrawCard();
                    return;
                } else {
                    // There is penalty.
                    server.playersTurn(-1);
                    moveToNextPlayer();
                    return;
                }
            }

            // The player wants to play out a card
            else {
                if (checkIfSetColor(i)) {
                    // first handle set color before proceeding to next player
                    gui.setColor();
                    server.playersTurn(i);
                    return;
                }
                server.playersTurn(i);
                moveToNextPlayer();
                return;
            }
        } else {
            gui.invalidCardError();
        }
    }

    /**
     * Check whether the new card is playable after the current player decides to draw.
     * This is done in controller in order to handle the case when the new card is WILD/WILD DRAW FOUR,
     * since it requires the current player to set color.
     */
    private void handleDrawCard() {
        int newCardIndex = server.getPlayerByIndex(server.getCurrentPlayer()).getCardCount() - 1;
        if (!server.validCardByIndex(newCardIndex)) {
            moveToNextPlayer();
            return;
        }

        //if the new card is playable, first check whether it is WILD/WILD DRAW FOUR
        if (checkIfSetColor(newCardIndex)) {
            gui.setColor();
            server.playersTurn(newCardIndex);
            return;
        }
        server.playersTurn(newCardIndex);
        moveToNextPlayer();

    }

    /**
     * Check whether the selected card is WILD/WILD DRAW FOUR
     */
    private boolean checkIfSetColor(int i) {
        if (server.getPlayerByIndex(server.getCurrentPlayer()).getCard(i).getSpecial() == Util.Special.WILD) {
            return true;
        }
        if (server.getPlayerByIndex(server.getCurrentPlayer()).getCard(i).getSpecial() == Util.Special.WILDDRAWFOUR) {
            return true;
        }
        return false;
    }

    /**
     * Update the gui according to the current state. This will be called after each player's turn.
     */
    private void updateGamePanel() {
        gui.updateGamePanel(server.getCurrColor(), server.getCurrNumber(), server.getCurrSpecial(),
                server.getCurrentPlayer(), server.getNextDraw(), server.getDrawPileNum(),
                server.getDiscardPileNum(), server.getHandCards(), new cardButtonListener());
    }


    /**
     * An action listener for every card button.
     * This function is used to pass the index of the pressed card button to controller.
     */
    public class cardButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < gui.getGamePanel().getCardButtons().size(); i++) {
                if (e.getSource() == gui.getGamePanel().getCardButtons().get(i)) {
                    handlePlayCardByIndex(i);
                }
            }
        }
    }
}
