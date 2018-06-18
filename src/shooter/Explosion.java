package shooter;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Explosion extends Entity{
    private static final int SHOCK_WAVE_SPEED = 120;
    private static final int MAX_RADIUS_FACTOR = 3;
    private int maxRadius;
    public Explosion(Game game, double x, double y, int radius) {
        super(game, x, y, radius, 0, 1, new Color(255,216,0,128));
        this.maxRadius = MAX_RADIUS_FACTOR * radius;
    }
    
    @Override
    public void update(double frameTime){
        this.radius += SHOCK_WAVE_SPEED * frameTime;
        if(this.radius >= this.maxRadius){
            this.destroy();
        }
    }
    
    /*@Override
    public void draw(Graphics2D g){
        
    }*/
}
