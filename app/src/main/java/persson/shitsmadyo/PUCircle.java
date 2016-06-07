package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Pontus on 2015-10-18.
 */
public class PUCircle extends GameObject {
    private Bitmap image;
    private Bitmap spritesheet;

    public PUCircle(Bitmap res, int x, int y, int w, int h) {
        super.x=x;
        super.y=y;
        width = w;
        height = h;

        spritesheet = res;

            image = Bitmap.createBitmap(spritesheet, 0, 0, width, height);
    }
    @Override
    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(image, x, y, null);
        }catch(Exception E){}
    }
    @Override
    public int getWidth(){
        return width;
    }
    @Override
    public int getHeight(){
        return height;
    }
    @Override
    public Bitmap getBitmap(){
        return spritesheet;
    }
}
