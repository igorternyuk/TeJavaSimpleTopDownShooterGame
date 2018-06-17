package shooter;

import java.util.Random;

/**
 *
 * @author igor
 */
public class Enemy extends Entity{
    private boolean isReady = false;
    private EnemyType type;
    private int rank;
    
    public Enemy(Game game, EnemyType type) {
        super(game, Math.random() * (Game.WINDOW_WIDTH - type.getRadius())
                + type.getRadius(), -type.getRadius(), type.getRadius(),
              type.getSpeed(), type.getLives(), type.getColor());
        this.type = type;
        this.rank = type.getRank();
        Random random = new Random();
        double angleInRadians = Math.toRadians(random.nextInt(90) + 30);
        this.vx = this.speed * Math.cos(angleInRadians);
        this.vy = this.speed * Math.sin(angleInRadians);
    }
    
    public Enemy(Game game, EnemyType type, double x, double y, int radius){
        this(game, type);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public EnemyType getType() {
        return type;
    }

    public int getRank() {
        return rank;
    }
    public boolean isIsReady() {
        return isReady;
    }
    
    public void explode(){
        this.radius *= 0.5;
        if(this.radius < 5){
            this.destroy();
        } else {
            for(int i = 0; i < 2; ++i){
                this.game.getEntities().add(new Enemy(this.game, this.type,
                        this.x, this.y, this.radius));
            }
        }
        
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
