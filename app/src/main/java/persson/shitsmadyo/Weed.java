package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Timer;
import java.util.TimerTask;

public class Weed extends PowerUp {
    private int speed;
    private int score;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    public Weed(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        score = s;

        speed = GamePanel.MOVESPEED;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for (int i = 0; i<image.length;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
    }
    @Override
    public void update(){
        speed = GamePanel.MOVESPEED;
        y+=speed;
        animation.update();
    }
    @Override
    public void draw(Canvas canvas) {
        try{
            Paint pm = new Paint();
            pm.setAntiAlias(true);
            pm.setFilterBitmap(true);
            canvas.drawBitmap(animation.getImage(), x, y, pm);
        }catch(Exception E){}
    }
    @Override
    public Bitmap getBitmap(){
        return spritesheet;
    }
//    public void activate(){
//        final int movespeed = GamePanel.MOVESPEED;
//        GamePanel.MOVESPEED = GamePanel.MOVESPEED/2;
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                GamePanel.MOVESPEED = movespeed;
//            }
//        }, 8000);
//    }
}
