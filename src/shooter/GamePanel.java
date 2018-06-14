package shooter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author igor
 */
public class GamePanel extends JPanel implements Runnable{
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final double FPS = 30;
    private Thread thread;
    private boolean isRunning;
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Font smallFont = new Font("Tahoma", Font.BOLD, 20);
    
    public GamePanel() {
        super();
        init();
    }
    
    private void init(){
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
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
            System.out.println("startTime = " + startTime);
            timeSinceLastUpdate += elapsedTime;
            System.out.println("timeSinceLastUpdate = " + timeSinceLastUpdate);
            while(timeSinceLastUpdate > frameTime){
                gameUpdate(frameTime);
                timeSinceLastUpdate -= frameTime;
            }
            gameRender();
            gameDraw();
            endTime = System.nanoTime();
            System.out.println("endTime = " + endTime);
            elapsedTime = (endTime - startTime) * 1e-9;
            System.out.println("Elapsed time = " + elapsedTime);
        }
        
        /*
        sf::Clock clock;
    sf::Time timeSinceLastUpdate = sf::Time::Zero;
    while(mWindow.isOpen())
    {
        auto elapsedTime = clock.restart();
        timeSinceLastUpdate += elapsedTime;
        while(timeSinceLastUpdate > mFrameTime)
        {
            timeSinceLastUpdate -= mFrameTime;
            processEvents();
            update(mFrameTime);
        }
        updateStatistics(elapsedTime);
        render();
    }
        */
    }
    
    private void gameUpdate(double frameTime){
        
    }
    
    private void gameRender(){
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2d.setColor(Color.white);
        this.g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.g2d.setColor(Color.red);
        this.g2d.setFont(this.smallFont);
        this.g2d.drawString("Test string", 100, 100);
        this.g2d.setColor(Color.blue);
        this.g2d.fillOval(10, 10, 50, 50);
    }
    
    private void gameDraw(){
        Graphics2D g2 = (Graphics2D)this.getGraphics();
        g2.drawImage(canvas, 0, 0, null);
        g2.dispose();
    }
    
}
