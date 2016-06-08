package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
/**
 * Created by Pontus on 2016-06-08.
 */
public class Smoke {
    private Bitmap image;
    private int x, y;
    private boolean weedActive;

    public Smoke (Bitmap res, int x, int y) {
        image = res;
        this.x=x;
        this.y=y;
        weedActive=false;

    }
    public void update(){
        if(weedActive) {
            x=x+2;
        }
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
    public void setWeedActive(boolean b) {
        weedActive = b;
    }
    public boolean getWeedActive(){
        return weedActive;
    }
    public void setX(int x){
        this.x=x;
    }

}
