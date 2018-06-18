package shooter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author igor
 */
public class Enemy extends Entity{
    private static final int MIN_RADIUS = 5;
    private static final int HIT_FLASH_TIME = 50;
    private static final double SLOWDOWN_FACTOR = 0.3;
    private static final Color HIT_COLOR = Color.red;
    private static final int TOP_DIRECTION_ANGLE = 90;
    private static final int MIN_ANGLE = 30;
    private boolean ready = false;
    private EnemyType type;
    private int rank;
    private boolean isHit = false;
    private long hitTimer = 0;
    private long hitFlashElapsedTime = 0;
    private boolean slowdown = false;
    
    public Enemy(Game game, EnemyType type) {
        super(game, Math.random() * (Game.WINDOW_WIDTH - type.getRadius())
                + type.getRadius(), -type.getRadius(), type.getRadius(),
              type.getSpeed(), type.getLives(), type.getColor());
        this.type = type;
        this.rank = type.getRank();
        Random random = new Random();
        double angleInRadians
                = Math.toRadians(random.nextInt(TOP_DIRECTION_ANGLE) + MIN_ANGLE);
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
    public boolean isReady() {
        return ready;
    }
    
    public boolean isSlowdown() {
        return this.slowdown;
    }

    public void setSlowdown(boolean slowdown) {
        this.slowdown = slowdown;
    }
    
    
    public void explode(){
        int factor = this.type.getExplosionFactor();
        this.radius /= factor;
        System.out.println("radius = " + radius);
        if(this.radius <= MIN_RADIUS){
            this.destroy();
        } else {
            for(int i = 0; i < factor; ++i){
                Enemy enemy = new Enemy(this.game, this.type,
                        this.x, this.y, this.radius);
                enemy.lives = enemy.type.getLives() / factor;
                this.game.getEntities().add(enemy);
            }
        }
    }
    
    @Override
    public void hit(int damage){
        super.hit(damage);
        this.isHit = true;
        this.hitTimer = System.nanoTime();
    }
    
    //frameTime in seconds
    @Override
    public void update(double frameTime){
        if(this.slowdown){
            this.x += SLOWDOWN_FACTOR * this.vx * frameTime;
            this.y += SLOWDOWN_FACTOR * this.vy * frameTime;
        } else {
            super.update(frameTime);
        }
        bounceFromWalls();
        
        if(!this.ready && this.y > this.radius){
            this.ready = true;
        }
        
        if(this.isHit){
            this.hitFlashElapsedTime
                    = (System.nanoTime() - this.hitTimer) / 1000000;
            if(this.hitFlashElapsedTime >= HIT_FLASH_TIME){
                this.isHit = false;
                this.hitTimer = 0;
                this.hitFlashElapsedTime = 0;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g){
        this.color = this.isHit ? HIT_COLOR : this.type.getColor();
        super.draw(g);
    }
}
