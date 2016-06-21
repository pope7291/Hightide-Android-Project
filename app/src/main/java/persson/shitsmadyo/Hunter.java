package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Pontus on 2016-06-09.
 */
public class Hunter extends GameObject{
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private ArrayList<Point> points;

    public long getSpearStartTime() {
        return spearStartTime;
    }

    public void setSpearStartTime(long spearStartTime) {
        this.spearStartTime = spearStartTime;
    }

    private long spearStartTime;

    public Hunter(Bitmap res, int x, int y, int w, int h, int numFrames) {
        super.x=x;
        super.y=y;
        width=w;
        height=h;
        spearStartTime = System.nanoTime();

        speed = GamePanel.MOVESPEED;

        Bitmap[] image=new Bitmap[numFrames];

        spritesheet=res;

        for(int i=0; i<image.length;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(150);

        points = new ArrayList<>();
    }
    @Override
    public void update(){
        speed=GamePanel.MOVESPEED;
        y+=speed;
        animation.update();
        if(y>GamePanel.HEIGHT){
            spritesheet.recycle();
        }
    }
    public void draw(Canvas canvas, Paint paint) {
        try{
            canvas.drawBitmap(animation.getImage(), x, y, paint);
        }catch(Exception E){}
    }
    @Override
    public int getWidth(){
        return width-25;
    }
    @Override
    public int getHeight(){
        return height-25;
    }
    @Override
    public Bitmap getBitmap(){
        return spritesheet;
    }
    @Override
    public Rect getRectangle(){
        return new Rect(x, y, x+(width), y+(height));
    }
    @Override
    public ArrayList<Point> getPoints(){
        return this.points;
    }
    public boolean createSpear(){
        long spearElapsed = (System.nanoTime() - spearStartTime) / 1000000;
        if(spearElapsed>(GamePanel.spawnRate*3000)){
            spearStartTime=System.nanoTime();
            return true;
        } else {
            return false;
        }
    }


}
