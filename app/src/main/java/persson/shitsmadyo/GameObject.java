package persson.shitsmadyo;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Pontus on 2015-05-05.
 */
public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dy;
    protected int dx;
    protected int width;
    protected int height;
    protected ArrayList<Point> points;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void update(){

    }
    public void draw(Canvas canvas){

    }

    public void setHeight(int height) {
        this.height = height;
    }
    public Rect getRectangle(){
        return new Rect(x, y, x+width, y+height);

    }
    public Bitmap getBitmap(){
        return null;
    }
    public Boolean isSpearThrow(){
        return true;
    }
    public void setSpearThrow(boolean spearThrow){

    }

    public ArrayList<Point> getPoints(){
        return points;
    }
    public boolean isDead(){
        return true;
    }
    public void explosion(Bitmap res, int numFrames){

    }
    public boolean isDestroyed(){
        return true;
    }
    public void setDestroyed(boolean destroyed){

    }
}
