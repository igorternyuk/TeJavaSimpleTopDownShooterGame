package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public class Text extends Entity{
    private String text;
    
    public Text(Game game, double x, double y, String text, Color color) {
        super(game, x, y, 0, 0, 1, color);
        this.text = text;
    }
    
}
