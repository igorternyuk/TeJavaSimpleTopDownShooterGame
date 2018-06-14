package shooter;

/**
 *
 * @author igor
 */
public enum Direction {
    LEFT(-1, 0),
    RIGHT(+1, 0),
    UP(0, -1),
    DOWN(0, +1),
    NO(0,0);
    
    private int dx, dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
