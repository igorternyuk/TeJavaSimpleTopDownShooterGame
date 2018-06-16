package shooter;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Player extends Entity{
    private static final int RADIUS = 16;
    private static final int BULLET_SPEED = 125;
    private static final int BULLET_DAMAGE = 1;
    private static final long SHOT_DELAY = 200;
    private static final int RECOVERY_TIME = 3000;
    private static final Color REGULAR_COLOR = Color.lightGray;
    private static final Color RECOVERY_COLOR = Color.red;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean firing = false;
    private long lastShotTime = System.nanoTime();
    private boolean recovering = false;
    private long recoveryTimer = 0;
    private long elapsedForRecoveryTime = 0;
    private int score = 0;
    
    public Player(Game game) {
        super(game, Game.WINDOW_WIDTH / 2, Game.WINDOW_HEIGHT / 2,
              RADIUS, 100, 3, REGULAR_COLOR);
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isFiring() {
        return firing;
    }

    public boolean isRecovering() {
        return recovering;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }
    
    public void addScore(int increment){
        this.score += increment;
    }
    
    @Override
    public void hit(){
        super.hit();
        this.recovering = true;
        this.color = RECOVERY_COLOR;
        this.recoveryTimer = System.nanoTime();
    }
    
    @Override
    public void update(double frameTime){
        if(this.movingLeft){
            this.vx = -this.speed;
        } else if(this.movingRight){
            this.vx = this.speed;
        }
        
        if(this.movingUp){
            this.vy = -this.speed;
        } else if(this.movingDown){
            this.vy = this.speed;
        }
        
        if(this.recovering){
            //System.out.println("We are recovering now");
            this.elapsedForRecoveryTime
                    = (System.nanoTime() - this.recoveryTimer) / 1000000;
            if(this.elapsedForRecoveryTime >= RECOVERY_TIME){
                this.recovering = false;
                this.recoveryTimer = 0;
                this.elapsedForRecoveryTime = 0;
            }
        } else {
            //System.out.println("We are not recovering now");
            if(this.firing){
                if((System.nanoTime() - this.lastShotTime) / 1000000 > SHOT_DELAY){
                    this.game.getEntities().add(new Bullet(this.game, this.x,
                            this.y, BULLET_SPEED, -90, BULLET_DAMAGE));
                    this.lastShotTime = System.nanoTime();
                }
            }
        }
        super.update(frameTime);
        keepInBounds();
        resetVelocityComponents();
    }
    
    @Override
    public void draw(Graphics2D g){
        if(this.recovering){
            int alpha = (int)(255 * Math.sin(Math.PI
                    * this.elapsedForRecoveryTime / RECOVERY_TIME));
            if(alpha < 0) alpha = 0;
            if(alpha > 255) alpha = 255;
            this.color = new Color(RECOVERY_COLOR.getRed(),
                    RECOVERY_COLOR.getGreen(),
                    RECOVERY_COLOR.getBlue(), alpha);
        } else {
            this.color = REGULAR_COLOR;
        }
        super.draw(g);
    }
}
