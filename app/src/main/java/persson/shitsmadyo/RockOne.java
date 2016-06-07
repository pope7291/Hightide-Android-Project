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
public class RockOne extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private Rect[] rectangles;
    private ArrayList<Point> points;
    private Thread thread;

    public RockOne(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        super.x=x;
        super.y=y;
        width = w;
        height = h;
        score = s;

        speed = GamePanel.MOVESPEED;

        //cap speed
        if(speed>40)speed = 40;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(150);

        //collision points
        points = new ArrayList<>();
//        Point p1 = new Point(x+56,y+25);
//        Point p2 = new Point(x+106,y+16);
//        Point p3 = new Point(x+140,y+24);
//        Point p4 = new Point(x+160,y+52);
//        Point p5 = new Point(x+159,y+83);
//        Point p6 = new Point(x+158,y+126);
//        Point p7 = new Point(x+173,y+160);
//        Point p8 = new Point(x+181,y+195);
//        Point p9 = new Point(x+176,y+235);
//        Point p10 = new Point(x+155,y+263);
//        Point p11 = new Point(x+119,y+266);
//        Point p12 = new Point(x+82,y+253);
//        Point p13 = new Point(x+41,y+259);
//        Point p14 = new Point(x+16,y+226);
//        Point p15 = new Point(x+13,y+185);
//        Point p16 = new Point(x+18,y+148);
//        Point p17 = new Point(x+14,y+111);
//        Point p18 = new Point(x+19,y+74);
//        Point p19 = new Point(x+32,y+52);
//        Point p20 = new Point(x+74,y+16);




        points.add(new Point(x+36,y+44));
        points.add(new Point(x+51,y+30));
        points.add(new Point(x+75,y+22));
        points.add(new Point(x+115,y+17));
        points.add(new Point(x+147,y+16));
        points.add(new Point(x+178,y+16));
        points.add(new Point(x+202,y+21));
        points.add(new Point(x+240,y+30));
        points.add(new Point(x+263,y+43));
        points.add(new Point(x+268,y+61));
        points.add(new Point(x+264,y+109));
        points.add(new Point(x+231,y+111));
        points.add(new Point(x+195,y+105));
        points.add(new Point(x+138,y+87));
        points.add(new Point(x+106,y+93));
        points.add(new Point(x+81,y+96));
        points.add(new Point(x+59,y+109));
        points.add(new Point(x+35,y+111));
        points.add(new Point(x+22,y+82));
        points.add(new Point(x+13,y+112));



    }
    @Override
    public void update(){
        speed = GamePanel.MOVESPEED;
        y+=speed;
        animation.update();
//        ExecutorService exec = Executors.newFixedThreadPool(1);
//        exec.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Creating new thread for ROCK");
//                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//                points.set(1, new Point(x+56,y+25));
//                System.out.println(points.get(1));
//                points.set(2, new Point(x + 106, y + 16));
//                points.set(3, new Point(x+140,y+24));
//                points.set(4, new Point(x+160,y+52));
//                points.set(5, new Point(x+159,y+83));
//                points.set(6, new Point(x+158,y+126));
//                points.set(7, new Point(x+173,y+160));
//                points.set(8, new Point(x+181,y+195));
//                points.set(9, new Point(x+176,y+235));
//                points.set(10, new Point(x+155,y+263));
//                points.set(11, new Point(x+119,y+266));
//                points.set(12, new Point(x+82,y+253));
//                points.set(13, new Point(x+41,y+259));
//                points.set(14, new Point(x+16,y+226));
//                points.set(15, new Point(x+13,y+185));
//                points.set(16, new Point(x+18,y+148));
//                points.set(17, new Point(x+14,y+111));
//                points.set(18, new Point(x+19,y+74));
//                points.set(19, new Point(x+32,y+52));
//                points.set(20, new Point(x + 74, y + 16));
//            }
//        });
//        exec.shutdown();

        //also update all POINTS in the arraylist (p1,p2,p3 etc)
        //Thread thread = new Thread() {

        //thread.start();

    }
//    @Override
//    public void run() {
//        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//            points.set(1, new Point(x+56,y+25));
//            points.set(2, new Point(x + 106, y + 16));
//            points.set(3, new Point(x+140,y+24));
//            points.set(4, new Point(x+160,y+52));
//            points.set(5, new Point(x+159,y+83));
//            points.set(6, new Point(x+158,y+126));
//            points.set(7, new Point(x+173,y+160));
//            points.set(8, new Point(x+181,y+195));
//            points.set(9, new Point(x+176,y+235));
//            points.set(10, new Point(x+155,y+263));
//            points.set(11, new Point(x+119,y+266));
//            points.set(12, new Point(x+82,y+253));
//            points.set(13, new Point(x+41,y+259));
//            points.set(14, new Point(x+16,y+226));
//            points.set(15, new Point(x+13,y+185));
//            points.set(16, new Point(x+18,y+148));
//            points.set(17, new Point(x+14,y+111));
//            points.set(18, new Point(x+19,y+74));
//            points.set(19, new Point(x+32,y+52));
//            points.set(20, new Point(x + 74, y + 16));
//        setCurrentThread((Thread.currentThread()));
//
//    }
    @Override
    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }catch(Exception E){}
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
