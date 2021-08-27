package unogui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class represents the start scene of the game
 * Update: turned this class from a JFrame to a JPanel. The StartPanel object will be constructed by GUI class
 */
public class StartPanel extends JPanel{
    private JLabel userLabel;
    private JTextField numPlayerText;
    private JLabel AILabel;
    private JTextField AIText;
    private JLabel label;
    private JButton startGameButton;
    private JLabel errorMessage;

    /**
     * construct a start scene.
     * The user will be able to enter any number between 2 and 10 to determine the number of players.
     */
    public StartPanel() {
        this.setLayout(null);

        userLabel = new JLabel("How Many Players?");
        userLabel.setBounds(310, 20, 200, 25);
        this.add(userLabel);

        numPlayerText = new JTextField();
        numPlayerText.setBounds(460, 20, 50, 25);
        this.add(numPlayerText);

        AILabel = new JLabel("How Many AIs?");
        AILabel.setBounds(310, 50, 200, 25);
        this.add(AILabel);

        AIText = new JTextField();
        AIText.setBounds(460, 50, 50, 25);
        this.add(AIText);

        label = new JLabel("2 <= Player + AI <= 10");
        label.setBounds(300, 80, 200, 25);
        this.add(label);

        startGameButton = new JButton("Play");
        startGameButton.setBounds(520, 20, 60, 25);
        this.add(startGameButton);

        errorMessage = new JLabel("Please enter a number between 2 to 10");
        errorMessage.setBounds(350, 100, 300, 25);
        this.add(errorMessage);
        errorMessage.setVisible(false);
    }

    public void addPlayListener(ActionListener action) {
        startGameButton.addActionListener(action);
    }

    /**
     * Get the player number that the user entered. Catch any invalid inputs and flash error message.
     * This method will be called by controller when the user clicks the Play button
     * @return number of the player and number of AI
     */
    public int[] getNumPlayer() {
        int numPlayer = -1;
        int numAI = 0;
        try {
            String numPlayerString = numPlayerText.getText();
            numPlayer = Integer.parseInt(numPlayerString);
            String numAIString = AIText.getText();
            numAI = Integer.parseInt(numAIString);
        } catch (Exception e) {
            errorMessage.setVisible(true);
        }
        // only return valid number
        if (numAI >= 0 && numPlayer > 0 && numPlayer + numAI >= 2 && numPlayer + numAI <= 10) {
            System.out.println(numPlayer + numAI);
            return new int[]{numPlayer, numAI};
        }
        errorMessage.setVisible(true);
        return new int[]{-1, -1};
    }

}
