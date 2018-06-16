package shooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private Random random = new Random();
    private Thread thread;
    private boolean isRunning;
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Font smallFont = new Font("Tahoma", Font.BOLD, 20);
    private Color colorBackground = new Color(0,148,255);
    private List<Entity> entities = new ArrayList<>();
    private Player player;
    
    
    public Game() {
        super();
        initGUI();
        this.player = new Player(this);
        this.entities.add(this.player);  
        createEnemies();
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

    @Override
    public void run() {
        this.isRunning = true;
        this.canvas = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        this.g2d = (Graphics2D)this.canvas.getGraphics();
        
        //Game loop
        System.nanoTime();
        final double frameTime = 1.0f / FPS; 
        long startTime, endTime;
        double timeSinceLastUpdate = 0;
        double elapsedTime = 0;
        while(this.isRunning){
            startTime = System.nanoTime();
            //System.out.println("startTime = " + startTime);
            timeSinceLastUpdate += elapsedTime;
            //System.out.println("timeSinceLastUpdate = " + timeSinceLastUpdate);
            while(timeSinceLastUpdate > frameTime){
                gameUpdate(frameTime);
                timeSinceLastUpdate -= frameTime;
            }
            gameRender();
            gameDraw();
            endTime = System.nanoTime();
            //System.out.println("endTime = " + endTime);
            elapsedTime = (endTime - startTime) * 1e-9;
            //System.out.println("Elapsed time = " + elapsedTime);
        }
    }
    
    private void createEnemies(){
        for(int i = 0; i < 5; ++i){
            EnemyType randomEnemyType = 
                    EnemyType.values()[random.nextInt(EnemyType.values().length)];
            this.entities.add(new Enemy(this, randomEnemyType));
        }
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
                        e.hit(b.getDamage());
                        b.destroy();
                    }

                    if(first instanceof Enemy && second instanceof Bullet){
                        Bullet b = (Bullet)second;
                        Enemy e = (Enemy)first;
                        e.hit(b.getDamage());
                        b.destroy();
                    }
                    break;
                }
            }
        }
    }
    
    private void gameUpdate(final double frameTime){
        this.entities.removeIf(e -> !e.isAlive());
        //this.entities.forEach(e -> e.update(frameTime));
        /*Iterator<Entity> it = this.entities.iterator();
        while(it.hasNext()){
            Entity e = it.next();
            e.update(frameTime);
        }*/
        for(int i = this.entities.size() - 1; i >= 0; --i){
            this.entities.get(i).update(frameTime);
        }
        checkCollisions();
        
        //System.out.println("Entity count = " + this.entities.size());
    }
    
    private void gameRender(){
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2d.setColor(this.colorBackground);
        this.g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.entities.forEach(e -> e.draw(g2d));
    }
    
    private void gameDraw(){
        Graphics2D g2 = (Graphics2D)this.getGraphics();
        g2.drawImage(canvas, 0, 0, null);
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            this.player.setIsMovingLeft(true);
        } else if(keyCode == KeyEvent.VK_RIGHT){
            this.player.setIsMovingRight(true);
        }
        if(keyCode == KeyEvent.VK_UP){
            this.player.setIsMovingUp(true);
        } else if(keyCode == KeyEvent.VK_DOWN){
            this.player.setIsMovingDown(true);
        }
        
        if(keyCode == KeyEvent.VK_SPACE){
            this.player.setIsFiring(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            this.player.setIsMovingLeft(false);
        } else if(keyCode == KeyEvent.VK_RIGHT){
            this.player.setIsMovingRight(false);
        }
        if(keyCode == KeyEvent.VK_UP){
            this.player.setIsMovingUp(false);
        } else if(keyCode == KeyEvent.VK_DOWN){
            this.player.setIsMovingDown(false);
        }
        if(keyCode == KeyEvent.VK_SPACE){
            this.player.setIsFiring(false);
        }

    }
    
}
