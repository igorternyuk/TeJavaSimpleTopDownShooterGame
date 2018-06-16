package shooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Player extends Entity{
    private static final int RADIUS = 16;
    private static final int BULLET_SPEED = 25;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isFiring = false;
    private long lastShotTime = System.nanoTime();
    private long shotDelay = 200;
    private Color color1 = Color.white;
    private Color color2 = Color.gray;

    public Player(Game game) {
        super(game, Game.WINDOW_WIDTH / 2, Game.WINDOW_HEIGHT / 2,
              RADIUS, 100, 3);
    }

    public void setIsMovingUp(boolean isMovingUp) {
        this.isMovingUp = isMovingUp;
    }

    public void setIsMovingDown(boolean isMovingDown) {
        this.isMovingDown = isMovingDown;
    }

    public void setIsMovingLeft(boolean isMovingLeft) {
        this.isMovingLeft = isMovingLeft;
    }

    public void setIsMovingRight(boolean isMovingRight) {
        this.isMovingRight = isMovingRight;
    }

    public void setIsFiring(boolean isFiring) {
        this.isFiring = isFiring;
    }
    
    @Override
    public void update(double frameTime){
        if(this.isMovingLeft){
            this.vx = -this.speed * frameTime;
        } else if(this.isMovingRight){
            this.vx = this.speed * frameTime;
        }
        
        if(this.isMovingUp){
            this.vy = -this.speed * frameTime;
        } else if(this.isMovingDown){
            this.vy = this.speed * frameTime;
        }
        
        super.update(frameTime);
        keepInBounds();
        resetVelocityComponents();
        
        if(this.isFiring){
            if((System.nanoTime() - this.lastShotTime) / 1000000 > this.shotDelay){
                this.game.getEntities().add(new Bullet(this.game, this.x,
                        this.y, BULLET_SPEED, -90));
                this.lastShotTime = System.nanoTime();
            }
        }
    }

    @Override
    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setStroke(new BasicStroke(3));
        g.setColor(color2);
        g.drawOval((int)this.x - this.radius, (int)this.y - this.radius,
                2 * this.radius, 2 * this.radius);
        g.setStroke(new BasicStroke(1));
    }
}