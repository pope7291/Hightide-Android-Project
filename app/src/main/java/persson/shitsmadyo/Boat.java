package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pontus on 2015-10-12.
 */
public class Boat extends GameObject {
    private int score;
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private ArrayList<Point> points;

    public Boat(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        score = s;

        speed = (GamePanel.MOVESPEED * 4);

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(200);

        //collision points
        points = new ArrayList<>();

        points.add(new Point(x+88,y+407));
        points.add(new Point(x+72,y+399));
        points.add(new Point(x+55,y+389));
        points.add(new Point(x+38,y+370));
        points.add(new Point(x+27,y+348));
        points.add(new Point(x+20,y+326));
        points.add(new Point(x+18,y+275));
        points.add(new Point(x+17,y+202));
        points.add(new Point(x+17,y+237));
        points.add(new Point(x+16,y+128));
        points.add(new Point(x+16,y+161));

        points.add(new Point(x+105,y+398));
        points.add(new Point(x+116,y+388));
        points.add(new Point(x+130,y+370));
        points.add(new Point(x+138,y+349));
        points.add(new Point(x+145,y+326));
        points.add(new Point(x+146,y+276));
        points.add(new Point(x+147,y+241));
        points.add(new Point(x+147,y+204));
        points.add(new Point(x+148,y+166));
        points.add(new Point(x+148,y+128));
        points.add(new Point(x+84,y+122));
    }
    @Override
    public void update() {
        speed = (GamePanel.MOVESPEED * 3);
        y += speed;
        animation.update();
        if (y > GamePanel.HEIGHT) {
            spritesheet.recycle();
        }
    }
    public void draw(Canvas canvas, Paint pm){
        try{
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
    public void collisionPoints(){
        points.set(0, new Point((x + 88), (y + 407)));
        points.set(1, new Point((x + 72), (y + 399)));
        points.set(2, new Point((x + 55), (y + 389)));
        points.set(3, new Point((x + 38), (y + 370)));
        points.set(4, new Point((x + 27), (y + 348)));
        points.set(5, new Point((x + 20), (y + 326)));
        points.set(6, new Point((x + 18), (y + 275)));
        points.set(7, new Point((x + 17), (y + 202)));
        points.set(8, new Point((x + 17), (y + 237)));
        points.set(9, new Point((x + 16), (y + 128)));
        points.set(10, new Point((x + 16), (y + 161)));
        points.set(11, new Point((x+ 105), (y + 398)));
        points.set(12, new Point((x+ 116), (y + 388)));
        points.set(13, new Point((x + 130), (y + 370)));
        points.set(14, new Point((x + 138), (y + 349)));
        points.set(15, new Point((x+ 145), (y + 326)));
        points.set(16, new Point((x + 146), (y + 276)));
        points.set(17, new Point((x + 147), (y + 241)));
        points.set(18, new Point((x + 147), (y + 204)));
        points.set(19, new Point((x + 148), (y + 166)));
        points.set(20, new Point((x+ 148), (y+ 128)));
        points.set(21, new Point((x + 84), (y + 122)));
    }
}
