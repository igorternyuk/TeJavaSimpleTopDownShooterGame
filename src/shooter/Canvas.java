package shooter;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author igor
 */
public class Canvas extends JPanel{
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 600;
    
    public Canvas() {
        super();
        init();
    }
    
    private void init(){
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
    }
    
}
