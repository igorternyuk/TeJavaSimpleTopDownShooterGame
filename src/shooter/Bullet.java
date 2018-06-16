package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public class Bullet extends Entity{
    private static final int RADIUS = 5;
    public Bullet(Game game, double x, double y, double speed, double angle) {
        super(game, x, y, RADIUS, speed, 1);
        this.vx = speed * Math.cos(Math.toRadians(angle));
        this.vy = speed * Math.sin(Math.toRadians(angle));
        this.color = Color.yellow;
    }
    
    public void destroy(){
        this.lives = 0;
    }
    
    @Override
    public void update(double frameTime){
        super.update(frameTime);
        if(isOutOfBounds()){
            destroy();
        }
    }
}
