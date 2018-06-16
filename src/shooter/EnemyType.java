package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum EnemyType {
    WEAK(Color.blue, 10, 40, 1, 3),
    MIDDLE(Color.orange, 15, 60, 2, 5),
    STRONG(Color.pink, 20, 80, 3, 7);
    
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
