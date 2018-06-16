package shooter;

import java.awt.Color;

/**
 *
 * @author igor
 */
public class Bullet extends Entity{
    private static final int RADIUS = 5;
    private int damage; 
    public Bullet(Game game, double x, double y, double speed, double angle,
            int damage) {
        super(game, x, y, RADIUS, speed, 1, Color.yellow);
        this.damage = damage;
        this.vx = this.speed * Math.cos(Math.toRadians(angle));
        this.vy = this.speed * Math.sin(Math.toRadians(angle));
    }

    public int getDamage() {
        return damage;
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
