package shooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Player {
    private double x, y;
    private int radius;
    private double vx, vy;
    private double speed;
    private Direction direction = Direction.NO;
    private int lives;
    private Color color1 = Color.white;
    private Color color2 = Color.gray;

    public Player() {
        this.x = GamePanel.WINDOW_WIDTH / 2;
        this.y = GamePanel.WINDOW_HEIGHT / 2;
        this.radius = 30;
        this.vx = 0;
        this.vy = 0;
        this.speed = 100;
        this.lives = 3;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public void update(double frameTime){
        this.vx = this.speed * this.direction.getDx() * frameTime;
        this.vy = this.speed * this.direction.getDy() * frameTime;
        //System.out.println("vx = " + vx + " vy = " + vy);
        this.x += this.vx;
        this.y += this.vy;
        keepInBounds();
        this.vx = 0;
        this.vy = 0;
    }
    
    private void keepInBounds(){
        if(this.x < this.radius)
            this.x = this.radius;
        else if(this.x + this.radius > GamePanel.WINDOW_WIDTH)
            this.x = GamePanel.WINDOW_WIDTH - this.radius;
        if(this.y < this.radius)
            this.y = this.radius;
        else if(this.y + this.radius > GamePanel.WINDOW_HEIGHT)
            this.y = GamePanel.WINDOW_HEIGHT - this.radius;
    }
    
    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval((int)this.x - this.radius, (int)this.y - this.radius,
                this.radius, this.radius);
        g.setStroke(new BasicStroke(3));
        g.setColor(color2);
        g.drawOval((int)this.x - this.radius, (int)this.y - this.radius,
                this.radius, this.radius);
        g.setStroke(new BasicStroke(1));
    }
    
    
}
