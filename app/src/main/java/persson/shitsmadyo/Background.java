package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

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

    //reset when image is off the screen
    public void update(){
        dy = GamePanel.MOVESPEED;
        y = y+dy;
        if(y>GamePanel.HEIGHT) {
            y=0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if(y>0) {
            canvas.drawBitmap(image, x, y - GamePanel.HEIGHT, null);
            //System.out.println("NU");
        }
        //System.out.println(y);
    }

}
