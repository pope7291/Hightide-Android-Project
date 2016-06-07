package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Pontus on 2015-05-04.
 */
public class MenuBG {
    private Bitmap image;
    private int x;
    private int y;

    public MenuBG(Bitmap res) {
        image = res;
        //image.createBitmap(image, 0, 0, 781, 1251);
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

}
