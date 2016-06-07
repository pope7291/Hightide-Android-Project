package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pontus on 2015-10-12.
 */
public class Smoke extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private Rect[] rectangles;
    private ArrayList<Point> points;
    private Thread thread;
    private boolean hide;

    public Smoke(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        score = s;


        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = Bitmap.createScaledBitmap(res, 9372, 1251, false);

        for(int i = 0; i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(100);
    }
    @Override
    public void update(){
        animation.update();
    }
    public void setHide(boolean hide){
        this.hide=hide;
    }
    public boolean getHide(){
        return hide;
    }
    @Override
    public void draw(Canvas canvas){
        if(!hide) {
            try {
                canvas.drawBitmap(animation.getImage(), x, y, null);
            } catch (Exception E) {
            }
        }
    }

    @Override
    public int getWidth(){
        //offset slightly for more realistic collision detection
        return width-25;
    }
    @Override
    public int getHeight(){
        return height-25;
    }
    @Override
    public Rect getRectangle(){
        return new Rect(x, y, x+(width), y+(height));
    }
    @Override
    public Bitmap getBitmap(){
        return spritesheet;
    }
    @Override
    public ArrayList<Point> getPoints(){
        return this.points;
    }
}
