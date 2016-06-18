package persson.shitsmadyo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public final float scaleFactorX = getWidth() / (WIDTH * 1.f);
    public final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
    public static final int WIDTH = 782;
    public static final int HEIGHT = 1251;
    public static int MOVESPEED = 15;
    private MainThread thread;
    private Background bg;
    private MenuBG menuBg;

    public Player getPlayer() {
        return player;
    }

    private Player player;

    public void setRock(ArrayList<GameObject> rock) {
        this.rock = rock;
    }

    //private Rect leftBorder = new Rect(0, 0, (int) (WIDTH * scaleFactorX / 3), (int) (HEIGHT * scaleFactorY));
    private ArrayList<GameObject> rock;
    private long rockStartTime;

    public ArrayList<Boat> getBoat() {
        return boat;
    }

    private ArrayList<Boat> boat;
    private ArrayList<Warning> warning;

    public ArrayList<PowerUp> getPowerup() {
        return powerup;
    }

    private ArrayList<PowerUp> powerup;

    public ArrayList<Hunter> getHunter() {
        return hunter;
    }

    private ArrayList<Hunter> hunter;
    private ArrayList<GameObject> spear;
    private long boatStartTime;
    private long hunterStartTime;
    private Random rand = new Random();
    private int click = 0;
    private long puStartTime;
    private Bitmap puempty;
    private Bitmap puweed;
    private Bitmap puvodka;
    private Bitmap pulsd;
    private Bitmap pusmall;
    private Bitmap pusmallsweed;
    private Bitmap pusmallvodka;
    private Bitmap pusmalllsd;
    private Bitmap mBitmap;
    private Spawner spawner;
    private int rockVariation;
    private int hunterVariation;
    private int powerUpVariation;
    private int boatVariation;
    private boolean updateOne = false;
    private boolean updateTwo = false;
    private ArrayList<PUCircle> powerupImages;
    public static double spawnRate;
    private boolean menu;
    public static boolean vodka;

    public MediaPlayer getWarningMP() {
        return warningMP;
    }

    MediaPlayer warningMP;

    public MediaPlayer getBoatMP() {
        return boatMP;
    }

    MediaPlayer boatMP;
    MediaPlayer weedPUMP;
    MediaPlayer vodkaPUMP;
    MediaPlayer weedMP;
    MediaPlayer vodkaMP;
    MediaPlayer death;
    MediaPlayer clickMP;
    MediaPlayer smackMP;
    MediaPlayer boatSmackMP;
    MediaPlayer breakMP;
    MediaPlayer pickupMP;
    MediaPlayer music;
    MediaPlayer lsdMP;
    //MediaPlayer lsdPUMP;
    Smoke smoke;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public float getScaleFactorX() {
        return scaleFactorX;
    }

    public float getScaleFactorY() {
        return scaleFactorY;
    }

    public void setMovespeed(int i) {
        MOVESPEED = i;
    }

    public void setVodka(boolean vodka) {
        this.vodka = vodka;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        menu();
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    public Smoke getSmoke() {
        return smoke;
    }

    public void gameStart() {
        menu = false;
        menuBg = null;
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.water));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.croc), BitmapFactory.decodeResource(getResources(), R.drawable.crocdead), 150, 200, 12);
        rock = new ArrayList<GameObject>();
        boat = new ArrayList<Boat>();
        warning = new ArrayList<Warning>();
        powerup = new ArrayList<PowerUp>();
        hunter = new ArrayList<Hunter>();
        spear = new ArrayList<GameObject>();
        rockStartTime = System.nanoTime();
        boatStartTime = System.nanoTime();
        puStartTime = System.nanoTime();
        hunterStartTime = System.nanoTime();
        rockVariation = 9 + (int)(Math.random() * ((11-9)+1));
        hunterVariation = 7 + (int)(Math.random() * ((13-7)+1));
        powerUpVariation = 6 + (int)(Math.random() * ((14-6)+1));
        boatVariation =   8 + (int)(Math.random() * ((12-8)+1));

        powerupImages = new ArrayList<>();
        spawnRate = 0.5;
        updateOne = false;
        updateTwo = false;
        vodka = false;
        smoke = new Smoke(BitmapFactory.decodeResource(getResources(), R.drawable.fog), -782, 0);
        spawner = new Spawner(this);

        //warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
        //boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
        //weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
        //vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
        //smackMP = MediaPlayer.create(this.getContext(), R.raw.smack);
        //weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
        //vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
        //death = MediaPlayer.create(this.getContext(), R.raw.death);
        //breakMP = MediaPlayer.create(this.getContext(), R.raw.breake);
        //pickupMP = MediaPlayer.create(this.getContext(), R.raw.pickup);
        //puempty = BitmapFactory.decodeResource(getResources(), R.drawable.pucircle);
        music = MediaPlayer.create(this.getContext(), R.raw.music);
        music.setLooping(true);
        music.start();
        music.setVolume(0.7f, 0.7f);
        puweed = BitmapFactory.decodeResource(getResources(), R.drawable.pucircleweed);
        pusmall = BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall);
        pusmallsweed = BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmallwedd);
        powerupImages.add(new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircle), WIDTH - 95, HEIGHT - 55, 50, 50));
        powerupImages.add(new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));
    }

    public void menu() {
        menu = true;
        clickMP = MediaPlayer.create(this.getContext(), R.raw.click);
        menuBg = new MenuBG(BitmapFactory.decodeResource(getResources(), R.drawable.menubg));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.croc), BitmapFactory.decodeResource(getResources(), R.drawable.crocdead), 150, 200, 12);
        //TEST
//        rock = null;
//        warning = null;
//        powerup = null;
//        rockStartTime = 0;
//        boatStartTime = 0;
//        puStartTime = 0;
//        powerupImages = null;
//        spawnRate = 0;
//        updateOne = false;
//        updateTwo = false;
//        puweed = null;
//        pusmall = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (menu) {
            final float scaleFactorX = getWidth() / (WIDTH * 1.f);
            final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
            //Rect left = new Rect(44, 164, 390, 117);
            Rect start = new Rect((int) (scaleFactorX * 44), (int) (scaleFactorY * 164), (int) (scaleFactorX * (390 + 44)), (int) (scaleFactorY * (117 + 164)));
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //Rect start = new Rect(scaleFactorX * WIDTH-44, scaleFactorX * WIDTH-164, scaleFactorY * HEIGHT-390, scaleFactorY * HEIGHT-117);
                if (start.contains(x, y)) {
                    clickMP.start();
                    gameStart();
                    return true;
                }
            }
        } else {
            if (player.getLifes() > 0) {
                int action = event.getActionMasked();
                final float scaleFactorX = getWidth() / (WIDTH * 1.f);
                final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
                int index = event.getActionIndex();
                Rect right = new Rect((int) ((WIDTH * scaleFactorX) / 3 + (int) (WIDTH * scaleFactorX) / 3), 0, (int) (WIDTH * scaleFactorX), (int) (HEIGHT * scaleFactorY));
                Rect middle = new Rect((int) (WIDTH * scaleFactorX / 3), 0, (int) (WIDTH * scaleFactorX), (int) (HEIGHT * scaleFactorY));
                Rect left = new Rect(0, 0, (int) (WIDTH * scaleFactorX / 3), (int) (HEIGHT * scaleFactorY));
                int pointerIndex = event.findPointerIndex(index);
                int x;
                int y;
                if (event.getPointerCount() > 1) {
                    x = (int) event.getX(pointerIndex);
                    y = (int) event.getY(pointerIndex);
                } else {
                    x = (int) event.getX();
                    y = (int) event.getY();
                }
                if (vodka) {
                    for (GameObject r : rock) {
                        Rect recta = new Rect((int) (r.getX() * scaleFactorX), (int) (r.getY() * scaleFactorY), (int) ((r.getX() + r.getWidth()) * scaleFactorX), (int) ((r.getY() + r.getHeight()) * scaleFactorY));
                        if (recta.contains(x, y)) {
                            breakMP = MediaPlayer.create(this.getContext(), R.raw.breake);
                            breakMP.start();
                            rock.remove(r);
                        }
                    }
                }
                if (action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN) {
                    if (right.contains(x, y) && player.getTooFarRight() == false) {
                        System.out.println("HOGERLOL");
                        if (!player.getPlaying()) {
                            player.setPlaying(true);
                        } else {
                            moveRight = true;
                            moveLeft = false;
                            player.setMovement(6);
                            player.setLeft(false);
                            player.setRight(true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player.setMovement(10);
                                }
                            }, 300);
                            player.setTooFarLeft(false);
                        }
                    }
                    if (left.contains(x, y) && player.getTooFarLeft() == false) {
                        System.out.println("VANSTERLOL");
                        if (!player.getPlaying()) {
                            player.setPlaying(true);
                        } else {
                            moveRight = false;
                            moveLeft = true;
                            player.setMovement(6);
                            player.setRight(false);
                            player.setLeft(true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player.setMovement(10);
                                }
                            }, 300);
                            player.setTooFarRight(false);
                        }
                    }
                    if (middle.contains(x, y)) {
                        if (!player.getPlaying()) {
                            player.setPlaying(true);
                        } else {
                            click++;
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    click = 0;
                                }
                            };

                            if (click == 1) {
                                //Single click
                                handler.postDelayed(r, 250);
                            } else if (click == 2) {
                                //Double click
                                click = 0;
                                if (!player.getPowerups().isEmpty() && !player.isPuActive()) {
                                    playPowerupSound(player.getPowerups().get(0));
                                    if (player.getPowerups().get(0) instanceof Weed) {
                                        smoke.setWeedActive(true);
                                        smoke.setX(-782);
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                smoke.setWeedActive(false);
                                            }
                                        }, 8000);
                                    }
                                    player.activate();
                                    //playSound(this.getContext(), R.raw.weed);
                                    updatePUImages();
                                }
                            }
                        }
                    }
                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    player.setRight(false);
                    player.setLeft(false);
                    return true;
                }
            }
        }
        return false;
    }

    public void moveRight() {
        player.setMovement(6);
        player.setLeft(false);
        player.setRight(true);
        player.setTooFarLeft(false);
    }

    public void moveLeft() {
        player.setMovement(6);
        player.setRight(false);
        player.setLeft(true);
        player.setTooFarRight(false);
    }

    public void update() {
        if (player.getPlaying() && player.getLifes() > 0 && !menu) {

            bg.update();
            player.update();
            if (smoke.getWeedActive()) {
                smoke.update();
            }
            //menuBg.update();
            //add rocks on timer
            long rockElapsed = (System.nanoTime() - rockStartTime) / 1000000;
            if (rockElapsed > spawnRate * 7000) {
                spawner.spawnRock();
                rockVariation = 9 + (int)(Math.random() * ((11-9)+1));
                rockStartTime = System.nanoTime();
            }
            //add boats on timer
            long boatElapsed = (System.nanoTime() - boatStartTime) / 1000000;
            if (boatElapsed > spawnRate * 10000) {
                spawner.spawnBoat();
                boatVariation =   8 + (int)(Math.random() * ((12-8)+1));
                // boatMP.start();
                boatStartTime = System.nanoTime();
            }

            //add hunters on timer
            long hunterElapsed = (System.nanoTime() - hunterStartTime) / 1000000;
            if (hunterElapsed > spawnRate * 10000) {
                spawner.spawnHunter();
                hunterVariation = 7 + (int)(Math.random() * ((13-7)+1));
                hunterStartTime = System.nanoTime();
                //ljudgrejer för hunter
            }

            //add powerups on timer
            long puElapsed = (System.nanoTime() - puStartTime) / 1000000;
            if (puElapsed > (spawnRate * 15000)) {
                spawner.spawnPowerUp();
                //reset timer
                puStartTime = System.nanoTime();
            }

            if (!player.isPuActive()) {
                if (player.getTime() > 60000 && !updateOne) {
                    MOVESPEED++;
                    spawnRate = spawnRate - 0.1;
                    updateOne = true;
                }
                if (player.getTime() > 120000 && !updateTwo) {
                    MOVESPEED++;
                    spawnRate = spawnRate - 0.1;
                    updateTwo = true;
                }
            }
            //loop through every rock and check collision and remove
            for (int i = 0; i < rock.size(); i++) {
                rock.get(i).update();
                if (collision(player, rock.get(i)) && !player.isDead()) {
                    smackMP = MediaPlayer.create(this.getContext(), R.raw.smack);
                    rock.get(i).getBitmap().recycle();
                    rock.remove(i);
                    smackMP.start();
                    death();
                    break;
                }
                //remove rock if it is way off the screen
                if (rock.get(i).getY() > HEIGHT * 2) {
                    rock.get(i).getBitmap().recycle();
                    rock.remove(i);
                    break;
                }
            }
            for (Hunter h : hunter) {
                if (h.getY() > HEIGHT * 2) {
                    h.getBitmap().recycle();
                    hunter.remove(h);
                }
                if (h.createSpear()) {
                    spear.add(new SpearDown(BitmapFactory.decodeResource(getResources(), R.drawable.spear), h.getX(), h.getY(), 15, 120, 1));
                }
            }
            int j = 0;
            while (j < boat.size()) {
                boat.get(j).update();
                if (collision(player, boat.get(j)) && !player.isDead()) {
                    boatSmackMP = MediaPlayer.create(this.getContext(), R.raw.boatsmack);
                    boat.get(j).getBitmap().recycle();
                    boat.remove(j);
                    boatSmackMP.start();
                    death();
                    break;
                }
                if (boat.get(j).getY() > HEIGHT * 2) {
                   // boatMP.stop();
                    boat.get(j).getBitmap().recycle();
                    boat.remove(j);
                    break;
                }
                j++;
            }
            for (int i = 0; i < hunter.size(); i++) {
                hunter.get(i).update();
            }
            for (int i = 0; i < warning.size(); i++) {
                warning.get(i).update();
            }
            for (int i = 0; i < spear.size(); i++) {
                spear.get(i).update();
            }
            for (int i = 0; i < powerup.size(); i++) {
                powerup.get(i).update();
                if (collisionPU(player, powerup.get(i))) {
                    pickupMP = MediaPlayer.create(this.getContext(), R.raw.pickup);
                    pickupMP.start();
//                    if(powerup.get(i) instanceof Weed) {
//                        //MediaPlayer weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
//                    } else if (powerup.get(i) instanceof Vodka) {
//                        //MediaPlayer vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
//                    }
                    player.addPowerup(powerup.get(i));
                    powerup.get(i).getBitmap().recycle();
                    powerup.remove(i);
                    player.setScore(player.getScore() + player.getScoreMultiplyer() * 5);
                    //powerup images
                    updatePUImages();
                    break;
                }
                if (powerup.get(i).getY() < -HEIGHT) {
                    powerup.get(i).getBitmap().recycle();
                    powerup.remove(i);
                    break;
                }
            }

            //Checks for left/right edge of the screen, puts player back at edge
            if (player.getX() <= 5) {
                player.setTooFarLeft(true);
                player.setX(5);
            }
            if (player.getX() >= WIDTH - 150) {
                player.setTooFarRight(true);
                player.setX(WIDTH - 150);
                //Is -59 ok? Or is it not scaleable
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            //Collision points. See RockCollision image.
            if (b instanceof Rock) {
                b.getPoints().set(0, new Point((b.getX() + 56), (b.getY() + 25)));
                b.getPoints().set(1, new Point((b.getX() + 106), (b.getY() + 16)));
                b.getPoints().set(2, new Point((b.getX() + 140), (b.getY() + 24)));
                b.getPoints().set(3, new Point((b.getX() + 160), (b.getY() + 52)));
                b.getPoints().set(4, new Point((b.getX() + 159), (b.getY() + 83)));
                b.getPoints().set(5, new Point((b.getX() + 158), (b.getY() + 126)));
                b.getPoints().set(6, new Point((b.getX() + 173), (b.getY() + 160)));
                b.getPoints().set(7, new Point((b.getX() + 181), (b.getY() + 195)));
                b.getPoints().set(8, new Point((b.getX() + 176), (b.getY() + 235)));
                b.getPoints().set(9, new Point((b.getX() + 155), (b.getY() + 263)));
                b.getPoints().set(10, new Point((b.getX() + 119), (b.getY() + 266)));
                b.getPoints().set(11, new Point((b.getX() + 82), (b.getY() + 253)));
                b.getPoints().set(12, new Point((b.getX() + 41), (b.getY() + 259)));
                b.getPoints().set(13, new Point((b.getX() + 16), (b.getY() + 226)));
                b.getPoints().set(14, new Point((b.getX() + 13), (b.getY() + 185)));
                b.getPoints().set(15, new Point((b.getX() + 18), (b.getY() + 148)));
                b.getPoints().set(16, new Point((b.getX() + 14), (b.getY() + 111)));
                b.getPoints().set(17, new Point((b.getX() + 19), (b.getY() + 74)));
                b.getPoints().set(18, new Point((b.getX() + 32), (b.getY() + 52)));
                b.getPoints().set(19, new Point((b.getX() + 74), (b.getY() + 16)));
                //exactly 20 points fucks it up for some reason
            }
            if (b instanceof RockOne) {
                b.getPoints().set(0, new Point((b.getX() + 36), (b.getY() + 44)));
                b.getPoints().set(1, new Point((b.getX() + 51), (b.getY() + 30)));
                b.getPoints().set(2, new Point((b.getX() + 75), (b.getY() + 22)));
                b.getPoints().set(3, new Point((b.getX() + 115), (b.getY() + 17)));
                b.getPoints().set(4, new Point((b.getX() + 147), (b.getY() + 16)));
                b.getPoints().set(5, new Point((b.getX() + 178), (b.getY() + 16)));
                b.getPoints().set(6, new Point((b.getX() + 202), (b.getY() + 21)));
                b.getPoints().set(7, new Point((b.getX() + 240), (b.getY() + 30)));
                b.getPoints().set(8, new Point((b.getX() + 263), (b.getY() + 43)));
                b.getPoints().set(9, new Point((b.getX() + 268), (b.getY() + 61)));
                b.getPoints().set(10, new Point((b.getX() + 264), (b.getY() + 109)));
                b.getPoints().set(11, new Point((b.getX() + 231), (b.getY() + 111)));
                b.getPoints().set(12, new Point((b.getX() + 195), (b.getY() + 105)));
                b.getPoints().set(13, new Point((b.getX() + 138), (b.getY() + 87)));
                b.getPoints().set(14, new Point((b.getX() + 106), (b.getY() + 93)));
                b.getPoints().set(15, new Point((b.getX() + 81), (b.getY() + 96)));
                b.getPoints().set(16, new Point((b.getX() + 59), (b.getY() + 109)));
                b.getPoints().set(17, new Point((b.getX() + 35), (b.getY() + 111)));
                b.getPoints().set(18, new Point((b.getX() + 22), (b.getY() + 82)));
            }
            if (b instanceof Boat) {
                b.getPoints().set(0, new Point((b.getX() + 88), (b.getY() + 407)));
                b.getPoints().set(1, new Point((b.getX() + 72), (b.getY() + 399)));
                b.getPoints().set(2, new Point((b.getX() + 55), (b.getY() + 389)));
                b.getPoints().set(3, new Point((b.getX() + 38), (b.getY() + 370)));
                b.getPoints().set(4, new Point((b.getX() + 27), (b.getY() + 348)));
                b.getPoints().set(5, new Point((b.getX() + 20), (b.getY() + 326)));
                b.getPoints().set(6, new Point((b.getX() + 18), (b.getY() + 275)));
                b.getPoints().set(7, new Point((b.getX() + 17), (b.getY() + 202)));
                b.getPoints().set(8, new Point((b.getX() + 17), (b.getY() + 237)));
                b.getPoints().set(9, new Point((b.getX() + 16), (b.getY() + 128)));
                b.getPoints().set(10, new Point((b.getX() + 16), (b.getY() + 161)));
                b.getPoints().set(11, new Point((b.getX() + 105), (b.getY() + 398)));
                b.getPoints().set(12, new Point((b.getX() + 116), (b.getY() + 388)));
                b.getPoints().set(13, new Point((b.getX() + 130), (b.getY() + 370)));
                b.getPoints().set(14, new Point((b.getX() + 138), (b.getY() + 349)));
                b.getPoints().set(15, new Point((b.getX() + 145), (b.getY() + 326)));
                b.getPoints().set(16, new Point((b.getX() + 146), (b.getY() + 276)));
                b.getPoints().set(17, new Point((b.getX() + 147), (b.getY() + 241)));
                b.getPoints().set(18, new Point((b.getX() + 147), (b.getY() + 204)));
                b.getPoints().set(19, new Point((b.getX() + 148), (b.getY() + 166)));
                b.getPoints().set(20, new Point((b.getX() + 148), (b.getY() + 128)));
                b.getPoints().set(21, new Point((b.getX() + 84), (b.getY() + 122)));

            }
            if (collisionTest(a, b)) {
                return true;
            }
        }
        return false;
    }

    public boolean collisionPU(GameObject player, PowerUp pu) {
        if (Rect.intersects(player.getRectangle(), pu.getRectangle())) {
            return true;
        }
        return false;
    }

    public void playPowerupSound(PowerUp pu) {
        if (pu instanceof Weed) {
            music.pause();
            //final MediaPlayer weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
            weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
            weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
            weedPUMP.start();
            weedMP.start();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    weedMP.stop();
                    weedPUMP.stop();
                    music.start();
                    //weedMP.reset();
                    //weedPUMP.reset();
                }
            }, 8000);
        } else if (pu instanceof Vodka) {
            music.pause();
            vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
            vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
            //final MediaPlayer vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
            vodkaPUMP.start();
            vodkaMP.start();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    vodkaMP.stop();
                    vodkaPUMP.stop();
                    music.start();
                    //vodkaMP.reset();
                    //vodkaPUMP.reset();
                }
            }, 8000);
        } else if (pu instanceof Lsd) {
            music.pause();
            //    lsdPUMP = MediaPlayer.create(this.getContext(), R.raw.lsdpickup);
            lsdMP = MediaPlayer.create(this.getContext(), R.raw.lsd);
            //  lsdPUMP.start();
            lsdMP.start();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    lsdMP.stop();
                    //   lsdPUMP.stop();
                    music.start();
                    //vodkaMP.reset();
                    //vodkaPUMP.reset();
                }
            }, 8000);
        }
    }


    public boolean collisionTest(GameObject player, GameObject thing) {
        for (Point p : thing.getPoints()) {
            if (player.getRectangle().contains(p.x, p.y) && !player.isDead()) {
                return true;
            }
        }
        return false;
    }

    public void death() {
        death = MediaPlayer.create(this.getContext(), R.raw.death);
        player.setIsDead(true);
        player.setLifes((player.getLifes() - 1));
        player.setScoreMultiplyer(1);
        if (player.getLifes() == 0) {
            player.setPlaying(false);
            warningMP.stop();
            boatMP.stop();
            vodkaMP.stop();
            vodkaPUMP.stop();
            weedMP.stop();
            weedPUMP.stop();
            lsdMP.stop();
            //  lsdPUMP.stop();
            music.stop();
            death.start();
            player.setSpritesheet(BitmapFactory.decodeResource(getResources(), R.drawable.crocdead));
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                death.stop();
                player.setIsDead(false);
            }
        }, 3000);
        //player.setSpritesheet(BitmapFactory.decodeResource(getResources(), R.drawable.croc));
        warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
        boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
        weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
        vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
        weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
        vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
        lsdMP = MediaPlayer.create(this.getContext(), R.raw.lsd);
        boatSmackMP = MediaPlayer.create(this.getContext(), R.raw.boatsmack);
        //TODO lsdPUMP när vi har ljudfil
        // lsdPUMP = MediaPlayer.create(this.getContext(), R.raw.lsdpickup);
    }

    public boolean collisionRock(Rect r, GameObject object) {
        if (Rect.intersects(r, object.getRectangle())) {
            return true;
        }
        return false;
    }
    public ArrayList<GameObject> getRockList(){
        return rock;
    }

    public void updatePUImages() {
        if (player.getPowerups().size() == 2) {
            if (player.getPowerups().get(0) instanceof Weed) {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircleweed), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else if (player.getPowerups().get(0) instanceof Vodka) {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclevodka), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else if (player.getPowerups().get(0) instanceof Lsd) {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource((getResources()), R.drawable.pucirclelsd), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircle), WIDTH - 95, HEIGHT - 55, 50, 50));
            }

            if (player.getPowerups().get(1) instanceof Weed) {
                powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmallwedd), WIDTH - 40, HEIGHT - 40, 35, 35));
            } else if (player.getPowerups().get(1) instanceof Vodka) {
                powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmallvodka), WIDTH - 40, HEIGHT - 40, 35, 35));
            } else if (player.getPowerups().get(1) instanceof Lsd) {
                powerupImages.set(1, new PUCircle((BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmalllsd)), WIDTH - 40, HEIGHT - 40, 35, 35));
            } else {
                powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));
            }
        } else if (player.getPowerups().size() == 1) {
            if (player.getPowerups().get(0) instanceof Weed) {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircleweed), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else if (player.getPowerups().get(0) instanceof Vodka) {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclevodka), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else if (player.getPowerups().get(0) instanceof Lsd) {
                powerupImages.set(0, new PUCircle((BitmapFactory.decodeResource(getResources(), R.drawable.pucirclelsd)), WIDTH - 95, HEIGHT - 55, 50, 50));
            } else {
                powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircle), WIDTH - 95, HEIGHT - 55, 50, 50));
            }
            powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));
            //powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));
        } else {
            powerupImages.set(0, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircle), WIDTH - 95, HEIGHT - 55, 50, 50));
            powerupImages.set(1, new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        //menuBg.draw(canvas);
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            Paint paintOriginal = new Paint();
            paintOriginal.setAntiAlias(true);
            paintOriginal.setFilterBitmap(true);
            LightingColorFilter test = new LightingColorFilter(0xFFFFFFFF, 0x000000FF);
            if (player.isLsd()) {
                paintOriginal.setColorFilter(test);
            }
            if (menuBg != null) {
                menuBg.draw(canvas);
            }
            bg.draw(canvas);
            player.draw(canvas);
            if (smoke.getWeedActive()) {
                smoke.draw(canvas);
            }
            for (GameObject r : rock) {
                r.draw(canvas);
            }
            for (Boat b : boat) {
                b.draw(canvas, paintOriginal);
            }
            for (Warning w : warning) {
                w.draw(canvas);
            }
            for (PowerUp p : powerup) {
                p.draw(canvas);
            }
            for (Hunter h : hunter) {
                h.draw(canvas, paintOriginal);
            }
            for (PUCircle pu : powerupImages) {
                pu.draw(canvas);
            }
            for (GameObject s : spear) {
                s.draw(canvas);
            }
            drawText(canvas);
            if (player.getLifes() == 0) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText("GAME OVER NIGGA", WIDTH / 2 - 200, HEIGHT / 2 - 50, paint);
            }
            canvas.restoreToCount(savedState);
        }
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if (player.isLsd()) {
            canvas.drawText("SCORE: " + ((int) player.getScore()) + " x" + player.getScoreMultiplyer() + " x2", 10, 40, paint);
        } else {
            canvas.drawText("SCORE: " + ((int) player.getScore()) + " x" + player.getScoreMultiplyer(), 10, 40, paint);
        }
        canvas.drawText("LIFES: " + player.getLifes(), WIDTH - 130, 40, paint);
    }
}