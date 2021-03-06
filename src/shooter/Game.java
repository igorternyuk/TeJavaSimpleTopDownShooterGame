package shooter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author igor
 */
public class Game extends JPanel implements KeyListener, Runnable{
    static final int WINDOW_WIDTH = 600;
    static final int WINDOW_HEIGHT = 600;
    private static final double FPS = 30;
    private static final int SLOWDOWN_TIME = 6000;
    private static final long WAVE_DELAY = 2000;
    private static final int MAX_WAVES = 10;
    private Random random = new Random();
    private Thread thread;
    private boolean isRunning;
    private BufferedImage canvas;
    private Graphics2D g2;
    private Font fontSmall = new Font("Tahoma", Font.BOLD, 24);
    private Font fontLarge = new Font("Verdana", Font.BOLD | Font.ITALIC, 48);
    private Color colorBackground = new Color(0,148,255);
    private Color colorPlayersLife = Color.green.darker();
    private Color colorWave = Color.white;
    private List<Entity> entities = new ArrayList<>();
    private Player player;    
    private long waveStartTime = 0;
    private long waveTimeDiff = 0;
    private boolean waveStarted = false;
    private int waveNumber = 0;
    private boolean enemySlowdown = false;
    private long slowDownTimer = 0;
    private long slowDownTimerDiff = 0;
    private GameState gameState = GameState.PLAY;
    
    public Game() {
        super();
        initGUI();
        this.player = new Player(this);
        this.entities.add(this.player);  
    }

    public List<Entity> getEntities() {
        return this.entities;
    }
    
    private void initGUI(){
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        if(this.thread == null){
            this.thread = new Thread(this);
            this.thread.start();
        }
    }
    
    private void startNewGame(){
        this.player.reset();
        this.entities.removeIf(e -> e != this.player);
        this.waveStartTime = 0;
        this.waveTimeDiff = 0;
        this.waveStarted = false;
        this.waveNumber = 0;
        this.enemySlowdown = false;
        this.slowDownTimer = 0;
        this.slowDownTimerDiff = 0;
        this.gameState = GameState.PLAY;
    }
    
    private void togglePause(){
        if(this.gameState.equals(GameState.PLAY)){
            this.gameState = GameState.PAUSED;
        } else if(this.gameState.equals(GameState.PAUSED)){
            this.gameState = GameState.PLAY;
        }
    }

    @Override
    public void run() {
        this.isRunning = true;
        this.canvas = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        this.g2 = (Graphics2D)this.canvas.getGraphics();
        
        //Game loop
        System.nanoTime();
        final double frameTime = 1.0f / FPS; 
        long startTime, endTime;
        double timeSinceLastUpdate = 0;
        double elapsedTime = 0;
        
        this.waveStartTime = 0;
        this.waveTimeDiff = 0;
        this.waveStarted = false;
        
        while(this.isRunning){
            startTime = System.nanoTime();
            timeSinceLastUpdate += elapsedTime;
            while(timeSinceLastUpdate > frameTime){
                if(gameState.equals(GameState.PLAY)){
                    gameUpdate(frameTime);
                }
                timeSinceLastUpdate -= frameTime;
            }
            gameRender();
            gameDraw();
            endTime = System.nanoTime();
            elapsedTime = (endTime - startTime) * 1e-9;
        }
    }
    
    private void createEnemies(){
        for(int i = 0; i < 2 * this.waveNumber; ++i){
            EnemyType randomEnemyType = 
                    EnemyType.values()[random.nextInt(EnemyType.values().length)];
            this.entities.add(new Enemy(this, randomEnemyType));
        }
    }
    
    private void addChanceForPowerUp(double x, double y){
        double rand = this.random.nextDouble();
        PowerUpType type = PowerUpType.ONE_LIFE;
        boolean maybeAddPowerUp = false;
        if(rand < 0.05){
            type = PowerUpType.ONE_LIFE;
            maybeAddPowerUp = true;
        } else if(rand < 0.08) {
            type = PowerUpType.SLOWDOWN;
            maybeAddPowerUp = true;
        } else if(rand < 0.10){
            type = PowerUpType.DOUBLE_POWER;
            maybeAddPowerUp = true;
        } else if(rand < 0.20){
            type = PowerUpType.POWER;
            maybeAddPowerUp = true;
        }
        
        if(maybeAddPowerUp){
            this.entities.add(new PowerUp(this, x, y, type));
        }
    }
    
    private void collectPowerUp(PowerUp e) {
        //System.out.println("PowerUp collected");
        PowerUpType type = e.getType();
        switch(type){
            case ONE_LIFE:
                this.player.gainLife();
                break;
            case POWER:
                this.player.increasePower(1);
                break;
            case DOUBLE_POWER:
                this.player.increasePower(2);
                break;
            case SLOWDOWN:
                setEnemySlowdown(true);
                break;
        }
        this.entities.add(new Text(this, e.getX(), e.getY(), type.getText(),
                type.getColor(), this.fontSmall, 2000, 4));
        e.destroy();
    }
    
    private void setEnemySlowdown(boolean slowdown){
        this.enemySlowdown = slowdown;
        this.slowDownTimer = System.nanoTime();
        this.entities.stream().filter(e -> e instanceof Enemy).forEach(e -> {
            Enemy enemy = (Enemy)e;
            enemy.setSlowdown(slowdown);
        });
    }
    
    private void handleBulletEnemyCollision(Bullet b, Enemy e) {
        if (e.isReady()) {
            e.hit(b.getDamage());
            if (!e.isAlive()) {
                handleDestroyedEnemy(e);
            }
            b.destroy();
        }
    }
    
    private void handleDestroyedEnemy(Enemy e){
        this.player.addScore(e.getRank());
        this.player.addKilledEnemy();
        if(e.getType().isSplittable()){
            e.split();
        }
        addChanceForPowerUp(e.getX(), e.getY());
        this.entities.add(new Explosion(this, e.getX(), e.getY(), e.getRadius()));
    }
    
    private void checkCollisions(){
        for(int i = 0; i < this.entities.size(); ++i){
            inner:
            for(int j = i + 1; j < this.entities.size(); ++j){
                Entity first = this.entities.get(i);
                Entity second = this.entities.get(j);
                if(first.collides(second)){
                    if(first instanceof Bullet && second instanceof Enemy){
                        Bullet b = (Bullet)first;
                        Enemy e = (Enemy)second;
                        handleBulletEnemyCollision(b, e);
                    }

                    if(first instanceof Enemy && second instanceof Bullet){
                        Bullet b = (Bullet)second;
                        Enemy e = (Enemy)first;
                        handleBulletEnemyCollision(b, e);
                    }
                    break;
                }
            }
        }
        
        for(int i = 0; i < this.entities.size(); ++i){
            Entity e = this.entities.get(i);
            if(!this.player.equals(e)){
                if(e.collides(this.player) && !this.player.isRecovering()){
                    if(e instanceof Enemy){
                        this.player.hit();
                        break;
                    }
                    
                    if(e instanceof PowerUp){
                        PowerUp p = (PowerUp)e;
                        collectPowerUp(p);
                        break;
                    }
                }
            }
        }
    }
    
    private void checkGameState(){
        if(!this.player.isAlive()){
            this.gameState = GameState.DEFEAT;
        } else {
            if(this.waveNumber > MAX_WAVES){
                this.gameState = GameState.VICTORY; 
            }
        }
    }
    
    private void gameUpdate(final double frameTime){
        long enemyCount = this.entities.stream()
                .filter(e -> e instanceof Enemy).count();
        if(this.waveStartTime == 0 && enemyCount == 0){
            ++this.waveNumber;
            this.waveStarted = false;
            this.waveStartTime = System.nanoTime();
        } else {
            this.waveTimeDiff
                    = (System.nanoTime() - this.waveStartTime) / 1000000;
            if(this.waveTimeDiff > WAVE_DELAY){
                this.waveStarted = true;
                this.waveStartTime = 0;
                this.waveTimeDiff = 0;
            }
        }
        
        if(this.waveStarted && enemyCount == 0){
            createEnemies();
        }
        
        if(this.enemySlowdown){
            this.slowDownTimerDiff
                    = (System.nanoTime() - this.slowDownTimer) / 1000000;
            if(this.slowDownTimerDiff >= SLOWDOWN_TIME){
                this.enemySlowdown = false;
                this.slowDownTimer = 0;
                this.slowDownTimerDiff = 0;
                setEnemySlowdown(false);
            }            
        }
        
        //Remove the dead entities
        this.entities.removeIf(e -> e != this.player && !e.isAlive());
        
        //Update all entitites
        for(int i = this.entities.size() - 1; i >= 0; --i){
            this.entities.get(i).update(frameTime);
        }
        
        //Collision handling
        checkCollisions();
        
        //Check if game over
        checkGameState();
    }
    
    private void gameRender(){
        this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2.setColor(this.colorBackground);
        this.g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        //Draw transparent layer in the slowdown mode
        if(this.enemySlowdown){
            this.g2.setColor(new Color(255,255,255,64));
            this.g2.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        
        //Draw all entities
        this.entities.forEach(e -> e.draw(g2));
        
        drawPlayerLifes();
        drawPlayerPower();
        drawPlayerScore();
        drawKilledEnemyCount();
        drawCurrentWaveNumber();
        drawWave();
        if(this.enemySlowdown)
            drawEnemySlowDown();
        drawGameState();
    }
    
    private void drawGameState(){
        g2.setFont(fontLarge);
        g2.setColor(this.gameState.getColor());
        String text = this.gameState.getText();
        int textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2)
                .getWidth();
        g2.drawString(text, (WINDOW_WIDTH - textWidth) / 2, WINDOW_HEIGHT / 2);
    }
    
    private void drawWave(){
        if(this.waveStartTime != 0){
            g2.setFont(fontLarge);
            String message = " - WAVE - " + this.waveNumber;
            Rectangle2D rect = g2.getFontMetrics().getStringBounds(message, g2);
            int messageHeight = (int)rect.getHeight();
            int messageWidth = (int)rect.getWidth();
            int alpha = (int)(255 * Math.sin(Math.PI
                    * this.waveTimeDiff / WAVE_DELAY));
            if(alpha < 0) alpha = 0;
            if(alpha > 255) alpha = 255;
            g2.setColor(new Color(colorWave.getRed(), colorWave.getGreen(),
                    colorWave.getBlue(), alpha));
            g2.drawString(message, (WINDOW_WIDTH - messageWidth) / 2,
                   (WINDOW_HEIGHT - messageHeight) / 2);
            
        }
    }
    
    private void drawPlayerLifes(){
        g2.setFont(fontSmall);
        g2.setColor(Color.blue.brighter());
        String livesStr = "LIVES: ";
        g2.drawString(livesStr, 30, 60);
        int lifeCount = this.player.getLives();
        g2.setColor(colorPlayersLife);
        for(int i = 0; i < lifeCount; ++i){
            g2.fillOval(40 * (i + 1) - 10, 80, 30, 30);
        }
    }
    
    private void drawPlayerScore(){
        g2.setFont(fontSmall);
        g2.setColor(Color.green.darker().darker());
        String scoreStr = "SCORE: " + this.player.getScore();
        int width = (int)g2.getFontMetrics().getStringBounds(scoreStr, g2)
                .getWidth();
        g2.drawString(scoreStr, WINDOW_WIDTH - width - 5, 30);
    }
    
    private void drawPlayerPower(){
        g2.setFont(fontSmall);
        g2.setColor(Color.orange.brighter());
        String powerStr = "POWER: ";
        g2.drawString(powerStr, 30, 145);
        g2.setColor(Color.yellow);
        g2.fillRect(30, 160, 30 * this.player.getPower(), 30);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.yellow.darker());
        for(int i = 0; i < this.player.getRequiredPower(); ++i){
            g2.drawRect(30 * (i + 1), 160, 30, 30);
        }
        g2.setStroke(new BasicStroke(1));
    }
    
    private void drawKilledEnemyCount(){
        g2.setFont(fontSmall);
        g2.setColor(Color.red.darker());
        String scoreStr = "KILLED ENEMIES: " + this.player.getKilledEnemyCount();
        int width = (int)g2.getFontMetrics().getStringBounds(scoreStr, g2)
                .getWidth();
        g2.drawString(scoreStr, WINDOW_WIDTH - width - 5, 60);
    }
    
    private void drawCurrentWaveNumber(){
        g2.setFont(fontSmall);
        g2.setColor(Color.blue);
        String text = "WAVE NUMBER: " + this.waveNumber;
        int textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2)
                .getWidth();
        g2.drawString(text, WINDOW_WIDTH - textWidth - 5, 90);
    }
    
    private void drawEnemySlowDown(){
        g2.setColor(Color.white);
        g2.setFont(fontSmall);
        String text = "SLOWDOWN: ";
        g2.drawString(text, 30, 215);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(30, 240, 100, 30);
        g2.setStroke(new BasicStroke(1));
        int currWidth = (int)(100 * (SLOWDOWN_TIME - this.slowDownTimerDiff)
                / SLOWDOWN_TIME);
        g2.fillRect(30, 240, currWidth, 30);
    }
    
    private void gameDraw(){
        Graphics2D g2d = (Graphics2D)this.getGraphics();
        g2d.drawImage(canvas, 0, 0, null);
        g2d.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            this.player.setMovingLeft(true);
        } else if(keyCode == KeyEvent.VK_RIGHT){
            this.player.setMovingRight(true);
        }
        if(keyCode == KeyEvent.VK_UP){
            this.player.setMovingUp(true);
        } else if(keyCode == KeyEvent.VK_DOWN){
            this.player.setMovingDown(true);
        }
        
        if(keyCode == KeyEvent.VK_SPACE){
            this.player.setFiring(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        switch (keyCode) {
            case KeyEvent.VK_P:
                togglePause();
                break;
            case KeyEvent.VK_N:
                startNewGame();
                break;
            case KeyEvent.VK_Q:
                this.isRunning = false;
                break;
            default:
                break;
        }
        
        if(keyCode == KeyEvent.VK_LEFT){
            this.player.setMovingLeft(false);
        } else if(keyCode == KeyEvent.VK_RIGHT){
            this.player.setMovingRight(false);
        }
        if(keyCode == KeyEvent.VK_UP){
            this.player.setMovingUp(false);
        } else if(keyCode == KeyEvent.VK_DOWN){
            this.player.setMovingDown(false);
        }
        if(keyCode == KeyEvent.VK_SPACE){
            this.player.setFiring(false);
        }
    }
}
