package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum GameState {
    PLAY("", Color.white),
    PAUSED("GAME PAUSED", Color.yellow),
    VICTORY("YOU WON!!!", Color.green.darker().darker()),
    DEFEAT("YOU LOST!", Color.red.darker().darker());
    
    String text;
    Color color;

    private GameState(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return this.text;
    }
    
    public Color getColor(){
        return this.color;
    }
}
