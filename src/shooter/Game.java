package shooter;

import javax.swing.JFrame;

/**
 *
 * @author igor
 */
public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JShooter");
        frame.setContentPane(new Canvas());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
