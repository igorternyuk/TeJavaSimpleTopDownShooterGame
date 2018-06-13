package shooter;

import javax.swing.JFrame;

/**
 *
 * @author igor
 */
public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JShooter");
        frame.setContentPane(new GamePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
