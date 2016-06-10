package persson.shitsmadyo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Player player;
    //private Rect leftBorder = new Rect(0, 0, (int) (WIDTH * scaleFactorX / 3), (int) (HEIGHT * scaleFactorY));
    private ArrayList<GameObject> rock;
    private long rockStartTime;
    private ArrayList<Boat> boat;
    private ArrayList<Warning> warning;
    private ArrayList<PowerUp> powerup;
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
    private boolean updateOne = false;
    private boolean updateTwo = false;
    private ArrayList<PUCircle> powerupImages;
    public static double spawnRate;
    private boolean menu;
    public static boolean vodka;
    MediaPlayer warningMP;
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
    public Smoke getSmoke(){
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

        powerupImages = new ArrayList<>();
        spawnRate = 0.5;
        updateOne = false;
        updateTwo = false;
        vodka = false;
        smoke = new Smoke(BitmapFactory.decodeResource(getResources(), R.drawable.fog), -782, 0);

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
            //return super.onTouchEvent(event);
        } else {
            if (player.getLifes() > 0) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN && vodka) {
//                    int x = (int) event.getX();
//                    int y = (int) event.getY();
//                    for (int i = 0; i < rock.size(); i++) {
//                        if (rock.get(i).getRectangle().contains(x, y)) {
//                            rock.remove(i);
//                        }
//                    }
//                    return true;
//                } else
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final float scaleFactorX = getWidth() / (WIDTH * 1.f);
                    final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
                    Rect right = new Rect((int) ((WIDTH * scaleFactorX) / 3 + (int) (WIDTH * scaleFactorX) / 3), 0, (int) (WIDTH * scaleFactorX), (int) (HEIGHT * scaleFactorY));
                    Rect middle = new Rect((int) (WIDTH * scaleFactorX / 3), 0, (int) (WIDTH * scaleFactorX), (int) (HEIGHT * scaleFactorY));
                    Rect left = new Rect(0, 0, (int) (WIDTH * scaleFactorX / 3), (int) (HEIGHT * scaleFactorY));
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if(vodka) {
                        for (GameObject r: rock) {
                            Rect recta = new Rect((int)(r.getX() * scaleFactorX),(int) (r.getY() * scaleFactorY), (int)((r.getX()+r.getWidth())*scaleFactorX), (int)((r.getY()+r.getHeight())*scaleFactorY));
                            if (recta.contains(x, y)) {
                                breakMP = MediaPlayer.create(this.getContext(), R.raw.breake);
                                breakMP.start();
                                rock.remove(r);
                            }
                        }
                    }
                if (right.contains(x, y) && player.getTooFarRight() == false) {
                        if (!player.getPlaying()) {
                            player.setPlaying(true);
                        } else {
                            player.setMovement(6);
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
                        return true;
                    }
                    if (left.contains(x, y) && player.getTooFarLeft() == false) {
                        if (!player.getPlaying()) {
                            player.setPlaying(true);
                        } else {
                            player.setMovement(6);
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
                        return true;
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
                                    if(player.getPowerups().get(0) instanceof Weed) {
                                        System.out.println("lmao");
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
                            return true;
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    player.setRight(false);
                    player.setLeft(false);
                    return true;
                }
                return super.onTouchEvent(event);
            }
            return true;
        }
        return false;
    }

    public void update() {
        if (player.getPlaying() && player.getLifes() > 0 && !menu) {
            bg.update();
            player.update();
            if(smoke.getWeedActive()) {
                smoke.update();
            }
            //menuBg.update();
            //add rocks on timer
            long rockElapsed = (System.nanoTime() - rockStartTime) / 1000000;
            if (rockElapsed > (spawnRate * 7000)) {
                boolean rockVar = rand.nextBoolean();
                if (rockVar) {
                    rock.add(new Rock(BitmapFactory.decodeResource(getResources(), R.drawable.rock), (int) (rand.nextDouble() * (WIDTH - 194)), -250, 194, 286, (int) player.getTime(), 7));
                } else {
                    rock.add(new RockOne(BitmapFactory.decodeResource(getResources(), R.drawable.rockone), (int) (rand.nextDouble() * (WIDTH - 238)), -250, 289, 139, (int) player.getTime(), 7));
                }
                //reset timer
                rockStartTime = System.nanoTime();
            }
            //add boats on timer
            long boatElapsed = (System.nanoTime() - boatStartTime) / 1000000;
            if (boatElapsed > (spawnRate * 10000)) {
          //      final int pos = (int) (rand.nextDouble() * (WIDTH - 262));
             //   warning.add(new Warning(BitmapFactory.decodeResource(getResources(), R.drawable.warning), pos + 70, 0, 150, 150, (int) player.getTime(), 3));
              //  System.out.println("Creating warning");
                boatStartTime = System.nanoTime();
                //MediaPlayer warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
                //final MediaPlayer boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
                warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
                boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
                warningMP.start();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        boolean correct = false;
                        boolean boatB;
                        int pos = (int) (rand.nextDouble() * (WIDTH - 210));
                        Rect puTemp = new Rect(pos, 0, pos + 210, GamePanel.HEIGHT);
                        while (!correct) {
                            boatB = true;
                            for (int i = 0; i < rock.size(); i++) {
                                if (collisionRock(puTemp, rock.get(i))) {
                                    boatB = false;
                                    pos = (int) (rand.nextDouble() * (WIDTH - 210));
                                    puTemp = new Rect(pos, 0, pos + 210, GamePanel.HEIGHT);
                                }
                            }
                            if (boatB) {
                                correct = true;
                            }
                        }
                        int boatVar = rand.nextInt(3);
                        if(boatVar==0) {
                            boat.add(new Boat(BitmapFactory.decodeResource(getResources(), R.drawable.boatarray), pos, -600, 210, 399, (int) player.getTime(), 7));
                        } else if(boatVar==1) {
                            boat.add(new Boat(BitmapFactory.decodeResource(getResources(), R.drawable.boatarraycoke), pos, -600, 210, 399, (int) player.getTime(), 7));
                        } else if(boatVar==2) {
                            boat.add(new Boat(BitmapFactory.decodeResource(getResources(), R.drawable.boatarrayhoes), pos, -600, 210, 399, (int) player.getTime(), 7));
                        }
//                        warning.remove(0);
                        boatMP.start();
                    }
                }, 1500);
                //warningMP.release();
            }

            //add hunters on timer
            long hunterElapsed = (System.nanoTime() - hunterStartTime) / 1000000;
            if(hunterElapsed > (spawnRate * 10000)) {
                boolean correct = false;
                boolean hunterB;
                int pos = (int) (rand.nextDouble()*(WIDTH - 125));

                Rect hunterTemp = new Rect(pos, 0, pos+125, 125);
                while(!correct) {
                    hunterB=true;
                    for(int i =0;i<rock.size();i++) {
                        if(collisionRock(hunterTemp, rock.get(i))) {
                            hunterB=false;
                            pos =(int)(rand.nextDouble() * (WIDTH-125));
                            hunterTemp = new Rect(pos, 0, pos+125, 125);
                        }
                    }
                    if(hunterB) {
                        correct=true;
                    }
                }
                hunter.add(new Hunter(BitmapFactory.decodeResource(getResources(), R.drawable.hunter), pos, -300, 125, 125, 1));
                hunterStartTime=System.nanoTime();
                //ljudgrejer för hunter

            }

            for(Hunter h:hunter) {
                if(h.getY()>HEIGHT*2) {
                    h.getBitmap().recycle();
                    hunter.remove(h);
                }
                if (h.createSpear()) {
                    spear.add(new SpearDown(BitmapFactory.decodeResource(getResources(), R.drawable.spear), h.getX(), h.getY(), 15, 120, 1));
                }
            }

            //add powerups on timer
            long puElapsed = (System.nanoTime() - puStartTime) / 1000000;
            if (puElapsed > (spawnRate * 15000)) {
                boolean correct = false;
                boolean rockB;
                int pos = (int) (rand.nextDouble() * (WIDTH - 100));
                //powerup.add(new Weed(BitmapFactory.decodeResource(getResources(), R.drawable.weed), pos, -200, 75, 75, (int) player.getTime(), 3));
                //check to make sure that the powerup doesn't spawn inside or just right next to a rock.
                Rect puTemp = new Rect(pos, 0, pos + 100, 100);
                while (!correct) {
                    rockB = true;
                    for (int i = 0; i < rock.size(); i++) {
                        if (collisionRock(puTemp, rock.get(i))) {
                            rockB = false;
                            pos = (int) (rand.nextDouble() * (WIDTH - 100));
                            puTemp = new Rect(pos, 0, pos + 100, 100);
                        }
                    }
                    if (rockB) {
                        correct = true;
                    }
                }
                int powerVar = rand.nextInt(4);
                //boolean powerVar = rand.nextBoolean();
                if (powerVar==1) {
                    powerup.add(new Weed(BitmapFactory.decodeResource(getResources(), R.drawable.weed), pos, 0, 100, 100, (int) player.getTime(), 8));
                } else if (powerVar==2) {
                    powerup.add(new Vodka(BitmapFactory.decodeResource(getResources(), R.drawable.vodka), pos, 0, 75, 75, (int) player.getTime(), 1));
                } else if (powerVar==3) {
                    powerup.add(new Lsd(BitmapFactory.decodeResource(getResources(), R.drawable.lsd), pos, 0, 75, 75, (int) player.getTime(), 1));
                }
                //powerup.add(new Weed(BitmapFactory.decodeResource(getResources(), R.drawable.weed), pos, 0, 75, 75, (int) player.getTime(), 3));
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
                if (rock.get(i).getY() > HEIGHT*2) {
                    rock.get(i).getBitmap().recycle();
                    rock.remove(i);
                    break;
                }
            }
            int j = 0;
            while (j<boat.size()) {
                boat.get(j).update();
                if (collision(player, boat.get(j)) && !player.isDead()) {
                    boatSmackMP = MediaPlayer.create(this.getContext(), R.raw.boatsmack);
                    boat.get(j).getBitmap().recycle();
                    boat.remove(j);
                    boatSmackMP.start();
                    death();
                    break;
                }
                if (boat.get(j).getY() > HEIGHT*2) {
                    boatMP.stop();
                    boat.get(j).getBitmap().recycle();
                    boat.remove(j);
                    break;
                }
                j++;
            }
            for(int i = 0; i<hunter.size();i++) {
                hunter.get(i).update();
            }
            for (int i = 0; i < warning.size(); i++) {
                warning.get(i).update();
            }
            for(int i = 0;i<spear.size();i++) {
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
                    player.setScore(player.getScore() + player.getScoreMultiplyer()*5);
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
            } if (b instanceof RockOne) {
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
            } if (b instanceof Boat) {
                b.getPoints().set(0, new Point((b.getX() + 128), (b.getY() + 553)));
                b.getPoints().set(1, new Point((b.getX() + 80), (b.getY() + 539)));
                b.getPoints().set(2, new Point((b.getX() + 52), (b.getY() + 514)));
                b.getPoints().set(3, new Point((b.getX() + 37), (b.getY() + 475)));
                b.getPoints().set(4, new Point((b.getX() + 31), (b.getY() + 435)));
                b.getPoints().set(5, new Point((b.getX() + 31), (b.getY() + 377)));
                b.getPoints().set(6, new Point((b.getX() + 31), (b.getY() + 319)));
                b.getPoints().set(7, new Point((b.getX() + 31), (b.getY() + 216)));
                b.getPoints().set(8, new Point((b.getX() + 31), (b.getY() + 203)));
                b.getPoints().set(9, new Point((b.getX() + 31), (b.getY() + 145)));
                b.getPoints().set(10, new Point((b.getX() + 31), (b.getY() + 87)));
                b.getPoints().set(11, new Point((b.getX() + 51), (b.getY() + 66)));
                b.getPoints().set(12, new Point((b.getX() + 90), (b.getY() + 46)));
                b.getPoints().set(13, new Point((b.getX() + 125), (b.getY() + 40)));
                b.getPoints().set(14, new Point((b.getX() + 158), (b.getY() + 42)));
                b.getPoints().set(15, new Point((b.getX() + 201), (b.getY() + 57)));
                b.getPoints().set(16, new Point((b.getX() + 230), (b.getY() + 88)));
                b.getPoints().set(17, new Point((b.getX() + 230), (b.getY() + 146)));
                b.getPoints().set(18, new Point((b.getX() + 230), (b.getY() + 204)));
                b.getPoints().set(19, new Point((b.getX() + 230), (b.getY() + 262)));
                b.getPoints().set(20, new Point((b.getX() + 230), (b.getY() + 320)));
                b.getPoints().set(21, new Point((b.getX() + 230), (b.getY() + 378)));
                b.getPoints().set(22, new Point((b.getX() + 230), (b.getY() + 436)));
                b.getPoints().set(23, new Point((b.getX() + 229), (b.getY() + 466)));
                b.getPoints().set(24, new Point((b.getX() + 215), (b.getY() + 502)));
                b.getPoints().set(25, new Point((b.getX() + 184), (b.getY() + 538)));
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
        if(pu instanceof Weed) {
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
        } else if(pu instanceof Vodka) {
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
        } else if (pu instanceof  Lsd) {
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
        if(player.getLifes()==0) {
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
            if (menuBg != null) {
                menuBg.draw(canvas);
            }
            bg.draw(canvas);
            player.draw(canvas);
            if(smoke.getWeedActive()) {
                smoke.draw(canvas);
            }
            for (GameObject r : rock) {
                r.draw(canvas);
            }
            for (Boat b : boat) {
                b.draw(canvas);
            }
            for (Warning w : warning) {
                w.draw(canvas);
            }
            for (PowerUp p : powerup) {
                p.draw(canvas);
            }
            for(Hunter h:hunter){
                h.draw(canvas);
            }
            for (PUCircle pu : powerupImages) {
                pu.draw(canvas);
            }
            for(GameObject s: spear) {
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