package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum PowerUpType {
    ONE_LIFE(10, 50, new Color(182, 255, 0)),
    POWER(20, 75, Color.yellow),
    DOUBLE_POWER(40, 100, new Color(255, 65, 0));
    
    private int radius;
    private int speed;
    private Color color;

    private PowerUpType(int radius, int speed, Color color) {
        this.radius = radius;
        this.speed = speed;
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public int getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }
}
