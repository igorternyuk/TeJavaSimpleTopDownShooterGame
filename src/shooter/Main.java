package shooter;

import javax.swing.JFrame;

/**
 *
 * @author igor
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JShooter");
        frame.setContentPane(new Game());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
