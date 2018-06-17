package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum EnemyType {
    WEAK(Color.blue, 10, 40, 1, 3),
    MIDDLE(Color.orange, 15, 60, 2, 5),
    STRONG(Color.pink, 20, 80, 3, 7),
    FAST_BUT_WEAK(Color.red, 10, 100, 2, 4),
    SLOW_BUT_HARD_TO_KILL(Color.green.darker(), 30, 20, 10, 12),
    EXPLODABLE(Color.magenta, 40, 80, 5, 10);

    private Color color;
    private int radius;
    private int speed;
    private int lives;
    private int rank;
    

    private EnemyType(Color color, int radius, int speed, int lives, int rank) {
        this.color = color;
        this.radius = radius;
        this.speed = speed;
        this.lives = lives;
        this.rank = rank;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public int getLives() {
        return lives;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRank() {
        return rank;
    }
    
}
