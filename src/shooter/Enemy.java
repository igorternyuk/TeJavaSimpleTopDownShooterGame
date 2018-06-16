package shooter;

import java.util.Random;

/**
 *
 * @author igor
 */
public class Enemy extends Entity{
    private boolean isReady = false;
    
    public Enemy(Game game, EnemyType type) {
        super(game, Math.random() * (Game.WINDOW_WIDTH - type.getRadius())
                + type.getRadius(), -type.getRadius(), type.getRadius(),
              type.getSpeed(), type.getLives(), type.getColor());
        Random random = new Random();
        double angleInRadians = Math.toRadians(random.nextInt(90) + 30);
        this.vx = this.speed * Math.cos(angleInRadians);
        this.vy = this.speed * Math.sin(angleInRadians);
    }

    public boolean isIsReady() {
        return isReady;
    }
    
    public void hit(){
        --this.lives;
    }
    
    @Override
    public void update(double frameTime){
        super.update(frameTime);
        bounceFromWalls();
        if(!this.isReady && this.y > this.radius){
            this.isReady = true;
        }
    }
}
