package shooter;

import java.awt.BasicStroke;
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
    protected Color color;
    
    public Entity(Game game, double x, double y, int radius, double speed,
            int lives, Color color) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.lives = lives;
        this.vx = 0;
        this.vy = 0;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public int getLives() {
        return lives;
    }
    
    public void hit(){
        --this.lives;
    }
    
    public void hit(int damage){
        this.lives -= damage;
    }
    
    public void destroy(){
        this.lives = 0;
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
    
    public boolean collides(Entity other){
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double radiusSumm = this.radius + other.radius;
        return dx * dx + dy * dy <= radiusSumm * radiusSumm;
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
    
    public void bounceFromWalls(){
        if((this.x < this.radius && this.vx < 0)
           || (this.x + this.radius > Game.WINDOW_WIDTH && this.vx > 0)){
            this.vx *= -1;
        }
        
        if((this.y < this.radius && this.vy < 0)
           || (this.y + this.radius > Game.WINDOW_HEIGHT && this.vy > 0)){
            this.vy *= -1;
        }
    }
    
    public void resetVelocityComponents(){
        this.vx = 0;
        this.vy = 0;
    }
    
    public void update(double frameTime){
        this.x += this.vx * frameTime;
        this.y += this.vy * frameTime;
    }
    
    public void draw(Graphics2D g){
        g.setColor(this.color);
        g.fillOval((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setStroke(new BasicStroke(3));
        g.setColor(this.color.darker());
        g.drawOval((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setStroke(new BasicStroke(1));
    }
}
