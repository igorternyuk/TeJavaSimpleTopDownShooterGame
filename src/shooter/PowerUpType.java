package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum PowerUpType {
    ONE_LIFE(10, 50, Color.green),
    POWER(20, 75, Color.yellow),
    DOUBLE_POWER(30, 100, Color.orange);
    
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
