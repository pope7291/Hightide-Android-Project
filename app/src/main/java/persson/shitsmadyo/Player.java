package persson.shitsmadyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pontus on 2015-05-05.
 */
public class Player extends GameObject {
    private Bitmap spritesheet;
    private Bitmap spritesheet2;
    private double score;
    private double time;
    private boolean right;
    private boolean left;
    private boolean playing;
    private Animation animation = new Animation();
    private Animation animationDead = new Animation();
    private long startTime;
    private int movement;
    private boolean tooFarLeft;
    private boolean tooFarRight;
    private ArrayList<PUCircle> powerupImages;
    private boolean puActive;
    private ArrayList<PowerUp> powerups;
    private int scoreMultiplyer;
    private long scoreStartTime;
    private long absStartTime;
    private int lifes;
    private boolean isDead;
    private boolean lsd = false;
    private int abstinens;

    public Player (Bitmap res, Bitmap res2, int w, int h, int numFrames) {
        x = (GamePanel.WIDTH / 2)-100;
        y = GamePanel.HEIGHT-165;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap [] imageDead = new Bitmap[numFrames];
        spritesheet2 = res2;
        for(int i = 0; i < imageDead.length; i++) {
            imageDead[i] = Bitmap.createBitmap(spritesheet2, i*width, 0, width, height);
            System.out.println(i*height);
            System.out.println(height);
        }

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;
        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
            System.out.println(i*height);
            System.out.println(height);
        }

        animation.setFrames(image);
        animation.setDelay(60);
        animationDead.setFrames(imageDead);
        animationDead.setDelay(60);
        startTime = System.nanoTime();
        powerups = new ArrayList<>();
        lifes = 3;
        setAbstinens(50);
       // powerupImages.add(new PUCircle(GamePanel.getPuempty()));

    }
    public void setMovement(int i) {
        this.movement = i;
    }
    public void setRight(boolean b) {
        right = b;
    }
    public void setLeft(boolean b) {
        left = b;
    }
    public void setSpritesheet(Bitmap res) {
        this.spritesheet = res;
    }
    public void update(){
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>500) {
            if (isLsd()) {
                score = score + ((0.5*scoreMultiplyer) * 2);
            } else {
                score = score + (0.5 * scoreMultiplyer);
            }
            time = time + 500;
            startTime = System.nanoTime();
        }
        long scoreElapsed = (System.nanoTime()-scoreStartTime)/1000000;
        if(scoreElapsed>20000) {
            scoreMultiplyer++;
            scoreStartTime = System.nanoTime();
        }
        long absElapsed = (System.nanoTime()-absStartTime)/1000000;
        if(absElapsed>750 && !isPuActive()) {
            if(abstinens!=0) {
                abstinens--;
            }
            absStartTime = System.nanoTime();
        }

        animation.update();
        animationDead.update();

        //movespeed
        if(right) {
        dx +=movement;
        }
        if(left) {
            dx -=movement;
        }
        if(dx>14) {
            dx=14;
        }
        if(dx<-14){
            dx=-14;
        }
        x +=dx*2;
        dx = 0;

//        //powerup images
//        if(powerups.get(0) instanceof Weed) {
//            powerupImages.set(0, new PUCircle(GamePanel.puweed));
//        } else if (powerups.get(0) == null) {
//            powerupImages.set(0, new PUCircle(GamePanel.puempty));
//        }


        }
    public ArrayList<PUCircle> getPowerupImages() {
        return powerupImages;
    }
    public void draw(Canvas canvas) {
        Paint pm = new Paint();
        pm.setAntiAlias(true);
        pm.setFilterBitmap(true);
        if(isDead) {
            canvas.drawBitmap(animationDead.getImage(), x, y, pm);
        } else {
            canvas.drawBitmap(animation.getImage(), x, y, pm);
        }
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public double getTime() {
        return time;
    }
    public boolean getPlaying(){
        return playing;
    }
    public void setPlaying(boolean b){
        this.playing=b;
    }
    public void resetDx(){
        dx = 0;
    }
    public void resetScore(){
        score = 0;
    }
    public int getX() {
        return x;
    }
    public void setScoreMultiplyer(int scoreMultiplyer) {
        this.scoreMultiplyer = scoreMultiplyer;
        scoreStartTime = System.nanoTime();
    }

    public int getScoreMultiplyer() {
        return scoreMultiplyer;
    }
    public void setPuActive(boolean puActive) {
        this.puActive = puActive;
    }

    public boolean isPuActive() {

        return puActive;
    }
    public void setTooFarLeft(boolean b){
        this.tooFarLeft = b;
    }
    public boolean getTooFarLeft(){
        return tooFarLeft;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setTooFarRight(boolean b) {
        this.tooFarRight = b;
    }
    public boolean getTooFarRight(){
        return tooFarRight;
    }
    public ArrayList<PowerUp> getPowerups() {
        return powerups;
    }
    public int getLifes() {
        return lifes;
    }
    public void setLifes(int lifes){
        this.lifes = lifes;
    }
    public void addPowerup(PowerUp powerup) {
        System.out.println("Adding" + powerups.size() + "" + powerups);
        if(powerups.size()==2) {
            //powerups.remove(0);
            powerups.set(0, powerups.get(1));
            //powerups.add(1, powerup);
            powerups.remove(1);
            powerups.add(1, powerup);
        } else if(powerups.size()==1) {
            powerups.add(1, powerup);
        } else {
            powerups.add(0, powerup);
        }
    }
    public void activate(){
        //spawn of rocks, boats and PUs must be adjusted to accomodate for the decreased speed, else the screen will be spammed with objects because they spawn at the same rate as usual but don't move as fast,
        //meaning they clunk up
        System.out.println("Activating" + powerups.size() + "" + powerups);
        if (powerups.get(0) instanceof Weed){
            final int movespeed = GamePanel.MOVESPEED;
            final double spawnrate = GamePanel.spawnRate;
            GamePanel.MOVESPEED = GamePanel.MOVESPEED/2;
            GamePanel.spawnRate = movespeed/GamePanel.MOVESPEED;
            puActive = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    GamePanel.MOVESPEED = movespeed;
                    GamePanel.spawnRate = spawnrate;
                    puActive = false;
                }
            }, 8000);
            if (powerups.size()==2) {
                //powerups.remove(0);
                powerups.set(0, powerups.get(1));
                powerups.remove(1);
            } else if(powerups.size()==1) {
                powerups.remove(0);
            }
        } else if (powerups.get(0) instanceof Vodka) {
            GamePanel.vodka = true;
            puActive = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    GamePanel.vodka = false;
                    puActive = false;
                }
            }, 8000);
            if (powerups.size()==2) {
                //powerups.remove(0);
                powerups.set(0, powerups.get(1));
                powerups.remove(1);
            } else if(powerups.size()==1) {
                powerups.remove(0);
            }

        } else if ( powerups.get(0) instanceof Lsd) {
            setLsd(true);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    setLsd(false);
                    puActive = false;
                }
            }, 8000);
            if (powerups.size()==2) {
                //powerups.remove(0);
                powerups.set(0, powerups.get(1));
                powerups.remove(1);
            } else if(powerups.size()==1) {
                powerups.remove(0);
            }
        }
    }
    @Override
    public Rect getRectangle(){
        return new Rect(x+25, y+30, x+110, y+140);
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean isLsd() {
        return lsd;
    }

    public void setLsd(boolean lsd) {
        this.lsd = lsd;
    }

    public int getAbstinens() {
        return abstinens;
    }

    public void setAbstinens(int abstinens) {
        this.abstinens = abstinens;
    }
}
