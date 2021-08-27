package unogui;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the winning scene and displays the winner.
 * Update: turned this class from a JFrame to a JPanel. The WinnerPanel object will be constructed by GUI class
 */
public class WinnerPanel extends JPanel{
    private int winnerIndex = 0;
    private JLabel label;

    /**
     * construct a winner scene
     * @param winner the index of the winner
     */
    public WinnerPanel(int winner) {
        winnerIndex = winner;


        this.setLayout(null);

        label = new JLabel("Winner is Player " + winnerIndex + "!");
        label.setBounds(250, 300,500, 100);
        label.setFont(new Font("Arial", Font.PLAIN, 50));
        this.add(label);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new WinnerPanel(2);
    }

}
