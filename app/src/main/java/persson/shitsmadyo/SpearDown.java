package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
/**
 * Created by Pontus on 2016-06-10.
 */
public class SpearDown extends GameObject {
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private ArrayList<Point> points;

    public SpearDown(Bitmap res, int x, int y, int w, int h, int numFrames) {
        super.x=x;
        super.y=y;
        width=w;
        height=h;

        speed=GamePanel.MOVESPEED;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet=res;

        for(int i =0;i<image.length;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(150);

        points = new ArrayList<>();
    }
    @Override
    public void update(){
        speed=GamePanel.MOVESPEED*3;
        y+=speed;
        animation.update();
        if(y>GamePanel.HEIGHT){
            spritesheet.recycle();
        }
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
}
