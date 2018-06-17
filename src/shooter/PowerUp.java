package shooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class PowerUp extends Entity{
    private static final long BLINK_PERIOD = 3000;
    private PowerUpType type;
    private long blinkTimerStart = System.nanoTime();
    private long blinkTimeElapsed = 0;
    public PowerUp(Game game, double x, double y, PowerUpType type) {
        super(game, x, y, type.getRadius(), type.getSpeed(), 1, type.getColor());
        this.type = type;
        this.vx = 0;
        this.vy = this.speed;
    }

    public PowerUpType getType() {
        return type;
    }
    
    @Override
    public void update(double frameTime){
        super.update(frameTime);
        if(isOutOfBounds()){
            destroy();
        }
        
        this.blinkTimeElapsed = (System.nanoTime() - this.blinkTimerStart) / 1000000;
        if(this.blinkTimeElapsed >= BLINK_PERIOD){
            this.blinkTimerStart = System.nanoTime();
            this.blinkTimeElapsed = 0;
        }
    }
    
    @Override
    public void draw(Graphics2D g){
        int alpha = (int)(255 * Math.sin(Math.PI
                    * this.blinkTimeElapsed / BLINK_PERIOD));
        if(alpha < 0) alpha = 0;
        if(alpha > 255) alpha = 255;
        this.color = new Color(this.color.getRed(), this.color.getGreen(),
                this.color.getBlue(), alpha);
            
        g.fillRect((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setColor(this.color.darker());
        g.setStroke(new BasicStroke(5));
        g.drawRect((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setStroke(new BasicStroke(1));
    }
}
