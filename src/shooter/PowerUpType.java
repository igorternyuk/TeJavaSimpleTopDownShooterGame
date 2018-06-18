package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum PowerUpType {
    ONE_LIFE(10, 50, Color.green, "Extra life"),
    POWER(15, 75, Color.yellow, "Power"),
    DOUBLE_POWER(20, 100, Color.orange, "Double power"),
    SLOWDOWN(20, 80, new Color(255, 0, 0, 127), "Slowdown");
    
    private int radius;
    private int speed;
    private Color color;
    private String text;

    private PowerUpType(int radius, int speed, Color color, String text) {
        this.radius = radius;
        this.speed = speed;
        this.color = color;
        this.text = text;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Color getColor() {
        return this.color;
    }
    
    public String getText(){
        return this.text;
    }
}
