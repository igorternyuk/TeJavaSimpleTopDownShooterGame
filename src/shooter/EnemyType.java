package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public enum EnemyType {
    WEAK(Color.blue, 10, 40, 1, 3, 0) {
        @Override
        public boolean isExplodable() {
            return false;
        }
    },
    MIDDLE(Color.orange, 15, 60, 2, 5, 0) {
        @Override
        public boolean isExplodable() {
            return false;
        }
    },
    STRONG(Color.pink, 20, 80, 3, 7, 0) {
        @Override
        public boolean isExplodable() {
            return false;
        }
    },
    FAST_BUT_WEAK(Color.red, 10, 100, 2, 4, 0) {
        @Override
        public boolean isExplodable() {
            return false;
        }
    },
    SLOW_BUT_HARD_TO_KILL(Color.green.darker(), 30, 20, 10, 9, 0) {
        @Override
        public boolean isExplodable() {
            return false;
        }
    },
    EXPLODABLE2(new Color(72, 0, 255, 128), 40, 80, 2, 10, 2) {
        @Override
        public boolean isExplodable() {
            return true;
        }
    },
    EXPLODABLE3(new Color(91, 127, 0, 128), 60, 60, 6, 12, 3) {
        @Override
        public boolean isExplodable() {
            return true;
        }
    },
    EXPLODABLE4(new Color(178, 0, 255, 128), 80, 40, 12, 15, 4) {
        @Override
        public boolean isExplodable() {
            return true;
        }
    };

    private Color color;
    private int radius;
    private int speed;
    private int lives;
    private int rank;
    private int explosionFactor;
    

    private EnemyType(Color color, int radius, int speed, int lives, int rank,
            int explosionFactor) {
        this.color = color;
        this.radius = radius;
        this.speed = speed;
        this.lives = lives;
        this.rank = rank;
        this.explosionFactor = explosionFactor;
    }
    
    public abstract boolean isExplodable();
    
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

    public int getExplosionFactor() {
        return explosionFactor;
    }
    
}
