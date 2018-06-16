package shooter;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Entity {
    protected Game game;
    protected double x, y;
    protected int radius;
    protected double speed;
    protected int lives;
    protected double vx, vy;
    protected Color color = Color.white;
    
    public Entity(Game game, double x, double y, int radius, double speed, int lives) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.lives = lives;
        this.vx = 0;
        this.vy = 0;
    }
    
    public boolean isOutOfBounds(){
        return this.x < this.radius
               || this.x + this.radius > Game.WINDOW_WIDTH
               || this.y < this.radius
               || this.y + this.radius > Game.WINDOW_HEIGHT;
    }
    
    public boolean isAlive(){
        return this.lives > 0;
    }
    
    public void keepInBounds(){
        if(this.x < this.radius)
            this.x = this.radius;
        else if(this.x + this.radius > Game.WINDOW_WIDTH)
            this.x = Game.WINDOW_WIDTH - this.radius;
        if(this.y < this.radius)
            this.y = this.radius;
        else if(this.y + this.radius > Game.WINDOW_HEIGHT)
            this.y = Game.WINDOW_HEIGHT - this.radius;
    }
    
    public void resetVelocityComponents(){
        this.vx = 0;
        this.vy = 0;
    }
    
    public void update(double frameTime){
        this.x += this.vx;
        this.y += this.vy;
    }
    
    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillOval((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
    }
}
