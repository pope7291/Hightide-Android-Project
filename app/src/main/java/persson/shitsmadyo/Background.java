package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * Created by Pontus on 2015-05-04.
 */
public class Background {
    private Bitmap image;
    private int x, y, dy;

    public Background (Bitmap res) {
        image = res;
        dy = GamePanel.MOVESPEED;
    }

    //reset when background is off screen
    public void update(){
        dy = GamePanel.MOVESPEED;
        y = y+dy;
        if(y>GamePanel.HEIGHT) {
            y=0;
        }
    }
    public int getY(){
        return y;
    }

    public void draw(Canvas canvas) {
        Paint pm = new Paint();
        pm.setAntiAlias(true);
        pm.setFilterBitmap(true);
        canvas.drawBitmap(image, x, y, pm);
        if(y>0) {
            canvas.drawBitmap(image, x, y - GamePanel.HEIGHT, pm);
        }
    }
    public Bitmap getBitmap(){
        return image;
    }

}
