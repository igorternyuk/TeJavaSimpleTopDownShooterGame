package shooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author igor
 */
public class Text extends Entity{
    private String text;
    private long time;
    private int blinkCount;
    private long blinkTimer;
    private long blinkTimerDiff = 0;
    private Font font;
    public Text(Game game, double x, double y, String text, Color color,
            Font font, long time, int blickCount) {
        super(game, x, y, 0, 0, 1, color);
        this.text = text;
        this.time = time;
        this.blinkCount = blickCount;
        this.font = font;
        this.blinkTimer = System.nanoTime();
    }
    
    @Override
    public void update(double frameTime){
        this.blinkTimerDiff = (System.nanoTime() - this.blinkTimer) / 1000000;
        if(this.blinkTimerDiff >= this.time){
            destroy();
        }
    }
    
    @Override
    public void draw(Graphics2D g){
        int alpha = (int)(255 * Math.sin(Math.PI * this.blinkTimerDiff / this.time * this.blinkCount));
        if(alpha < 0) alpha = 0;
        if(alpha > 255) alpha = 255;
        g.setFont(this.font);
        g.setColor(new Color(this.color.getRed(), this.color.getGreen(),
                this.color.getBlue(), alpha));
        int textWidth = (int)g.getFontMetrics().getStringBounds(this.text, g).getWidth();
        g.drawString(this.text, (int)(this.x - textWidth / 2), (int)this.y);
    }
    
}
