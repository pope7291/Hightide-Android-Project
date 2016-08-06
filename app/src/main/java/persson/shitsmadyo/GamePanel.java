package persson.shitsmadyo;

import android.app.Activity;
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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

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
    private Game act;
    private Spawner spawner;
    private Smoke smoke;
    private Player player;
    private String timerCurrent;
    private Random rand = new Random();
    private int click = 0;
    private boolean paused = false, go = false, moveLeft = false, moveRight = false, gameStarted = false, updateOne = false, updateTwo = false;
    public static boolean vodka;
    private MediaPlayer boatMP, weedPUMP, vodkaPUMP, weedMP, vodkaMP, death, clickMP, smackMP, boatSmackMP, breakMP, pickupMP, music, lsdMP, warningMP;
    //TODO MediaPlayer lsdPUMP;
    private long rockStartTime, boatStartTime, hunterStartTime, pauseStartTime, puStartTime;
    private Paint paintText, paintLsd, paintTimer;
    private double rockVariation, hunterVariation, powerUpVariation, boatVariation;
    public static double spawnRate;
    private ArrayList<GameObject> rock;
    private ArrayList<Boat> boat;
    private ArrayList<PowerUp> powerup;
    private ArrayList<Warning> warning;
    private ArrayList<Hunter> hunter;
    private ArrayList<GameObject> spear;
    private ArrayList<PUCircle> powerupImages;
    private ArrayList<MediaPlayer> sounds = new ArrayList<>();
    private ArrayList<MediaPlayer> soundsToStart = new ArrayList<>();
    private Bitmap puempty;
    private Bitmap puweed;
    private Bitmap puvodka;
    private Bitmap pulsd;
    private Bitmap pusmall;
    private Bitmap pusmallsweed;
    private Bitmap pusmallvodka;
    private Bitmap pusmalllsd;
    private Bitmap mBitmap;

    public Player getPlayer() {
        return player;
    }
    public ArrayList<Boat> getBoat() {
        return boat;
    }
    public ArrayList<PowerUp> getPowerup() {
        return powerup;
    }
    public ArrayList<Hunter> getHunter() {
        return hunter;
    }
    public MediaPlayer getWarningMP() {
        return warningMP;
    }
    public MediaPlayer getBoatMP() {
        return boatMP;
    }
    public float getScaleFactorX() {
        return scaleFactorX;
    }
    public float getScaleFactorY() {
        return scaleFactorY;
    }
    public Smoke getSmoke() {
        return smoke;
    }
    public void setMovespeed(int i) {
        MOVESPEED = i;
    }
    public void setVodka(boolean vodka) {
        this.vodka = vodka;
    }

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public GamePanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
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
        gameStart();
        thread.setRunning(true);
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }

    }

    public void gameStart() {
        final Handler sec = new Handler();
        sec.postDelayed(new Runnable() {
            @Override
            public void run() {
                clickMP = MediaPlayer.create(getContext(), R.raw.click);
                paintTimer = new Paint();
                paintTimer.setAntiAlias(true);
                paintTimer.setFilterBitmap(true);
                paintTimer.setColor(Color.WHITE);
                paintTimer.setShadowLayer(2, 0, 0, Color.BLACK);
                paintTimer.setTextSize(100);
                paintTimer.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/DJGROSS.ttf"));
                bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.water));
                go=true;
                timerCurrent="3";
            }
        }, 500);
        final Handler oneSec = new Handler();
        oneSec.postDelayed(new Runnable() {
            @Override
            public void run() {
                timerCurrent="2";
                clickMP.start();
            }
        }, 1500);
        final Handler twoSec = new Handler();
        twoSec.postDelayed(new Runnable() {
            @Override
            public void run() {
                timerCurrent="1";
                clickMP.start();
            }
        }, 2500);
        final Handler threeSec = new Handler();
        threeSec.postDelayed(new Runnable() {
            @Override
            public void run() {
                timerCurrent="GO!";
                music.setLooping(true);
                music.start();
                music.setVolume(0.7f, 0.7f);
                gameStarted = true;
                player.setPlaying(true);
                clickMP.start();
            }
        }, 3500);
        final Handler fourSec = new Handler();
        fourSec.postDelayed(new Runnable() {
            @Override
            public void run() {
                go=false;
            }
        }, 4500);
        breakMP = MediaPlayer.create(this.getContext(), R.raw.breake);
        smackMP = MediaPlayer.create(this.getContext(), R.raw.smack);
        boatSmackMP = MediaPlayer.create(this.getContext(), R.raw.boatsmack);
        pickupMP = MediaPlayer.create(this.getContext(), R.raw.pickup);
        weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
        weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
        lsdMP = MediaPlayer.create(this.getContext(), R.raw.lsd);
        warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
        death = MediaPlayer.create(this.getContext(), R.raw.death);
        vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
        vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
        boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
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
        rockVariation = (double)(rand.nextInt((11 - 9) + 1)+9)/10; //0.9-1.1
        hunterVariation = (double)(rand.nextInt((13 - 7) + 1)+7)/10; //0.7-1.3
        powerUpVariation = (double)(rand.nextInt((14 - 8) + 1)+8)/10; //0.8-1.4
        boatVariation = (double)(rand.nextInt((12 - 8) + 1)+8)/10; //0.8-1.2

        powerupImages = new ArrayList<>();
        spawnRate = 0.5;
        updateOne = false;
        updateTwo = false;
        vodka = false;
        smoke = new Smoke(BitmapFactory.decodeResource(getResources(), R.drawable.fog), -782, 0);
        spawner = new Spawner(this);

        music = MediaPlayer.create(this.getContext(), R.raw.music);

        puweed = BitmapFactory.decodeResource(getResources(), R.drawable.pucircleweed);
        pusmall = BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall);
        pusmallsweed = BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmallwedd);
        powerupImages.add(new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucircle), WIDTH - 95, HEIGHT - 55, 50, 50));
        powerupImages.add(new PUCircle(BitmapFactory.decodeResource(getResources(), R.drawable.pucirclesmall), WIDTH - 40, HEIGHT - 40, 35, 35));

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setFilterBitmap(true);
        paintText.setColor(Color.WHITE);
        paintText.setShadowLayer(2, 0, 0, Color.BLACK);
        paintText.setTextSize(30);
        paintText.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/justice.ttf"));

        paintLsd = new Paint();
        paintLsd.setAntiAlias(true);
        paintLsd.setFilterBitmap(true);
        paintLsd.setColor(Color.MAGENTA);
        paintLsd.setShadowLayer(2, 0, 0, Color.BLACK);
        paintLsd.setTextSize(40);
        //  Typeface font = Typeface.createFromAsset(getAssets(), "justiceout.ttf");
        paintLsd.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/justice.ttf"));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                            breakMP.start();
                            r.explosion(BitmapFactory.decodeResource(getResources(), R.drawable.rockonearraynewexplosion), 7);
                            r.setDestroyed(true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for(GameObject r:rock) {
                                        if(r.isDestroyed()) {
                                            rock.remove(r);
                                        }
                                    }
                                }
                            }, 420); //420 time required to complete animation. Probably need a better solution later
                        }
                    }
                }
                if (action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN) {
                    if (right.contains(x, y) && player.getTooFarRight() == false) {
                            moveRight = true;
                            moveLeft = false;
                            player.setMovement(movementAbs());
                            player.setLeft(false);
                            player.setRight(true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player.setMovement(movementHoldAbs());
                                }
                            }, 300);
                            player.setTooFarLeft(false);
                    }
                    if (left.contains(x, y) && player.getTooFarLeft() == false) {
                            moveRight = false;
                            moveLeft = true;
                            player.setMovement(movementAbs());
                            player.setRight(false);
                            player.setLeft(true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    player.setMovement(movementHoldAbs());
                                }
                            }, 300);
                            player.setTooFarRight(false);
                    }
                    if (middle.contains(x, y)) {
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
                                    updatePUImages();
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
        return false;
    }

   public int movementAbs() {
       int abstinens = player.getAbstinens();
       if (abstinens < 21) {
           return 4;
       } else if (abstinens < 41) {
           return 5;
       } else if (abstinens < 61) {
           return 6;
       } else if (abstinens < 81) {
           return 7;
       } else if (abstinens <= 100) {
           return 8;
       }
       return 6;
   }
    public int movementHoldAbs() {
        int abstinens = player.getAbstinens();
        if (abstinens < 21) {
            return 8;
        } else if (abstinens < 41) {
            return 10;
        } else if (abstinens < 61) {
            return 12;
        } else if (abstinens < 81) {
            return 14;
        } else if (abstinens <= 100) {
            return 16;
        }
        return 6;
    }
    public void update() {
        if (player.getPlaying() && player.getLifes() > 0 && gameStarted) {
            String score = (int)player.getScore()+" ";
            String scoreMulti = "x"+(int)player.getScoreMultiplyer()+" ";
            String abs = " "+player.getAbstinens();
            String lifes = " "+player.getLifes();
            act.setScoreText(score);
            act.setScoreMultiText(scoreMulti);
            act.setAbs(abs);
            act.setLifes(lifes);

            bg.update();
            player.update();
            if (smoke.getWeedActive()) {
                smoke.update();
            }
            if(player.getAbstinens()>=100) {
                death();
                player.setAbstinens(75);
            }
            //add rocks on timer
            long rockElapsed = (System.nanoTime() - rockStartTime) / 1000000;
            if (rockElapsed > spawnRate * 5000*rockVariation) {
                spawner.spawnRock();
                rockVariation = (double)(rand.nextInt((11 - 9) + 1)+9)/10; //0.9-1.1
                rockStartTime = System.nanoTime();
            }
            //add boats on timer
            long boatElapsed = (System.nanoTime() - boatStartTime) / 1000000;
            if (boatElapsed > spawnRate * 10000*boatVariation) {
                warningMP.start();
                spawner.spawnBoat();
                boatVariation = (double)(rand.nextInt((12 - 8) + 1)+8)/10; //0.8-1.2
                // boatMP.start();
                boatStartTime = System.nanoTime();
                final Handler boatSec = new Handler();
                boatSec.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boatMP.start();
                    }
                }, 1500);
            }

            //add hunters on timer
            long hunterElapsed = (System.nanoTime() - hunterStartTime) / 1000000;
            if (hunterElapsed > spawnRate * 10000*hunterVariation) {
                spawner.spawnHunter();
                hunterVariation = (double)(rand.nextInt((13 - 7) + 1)+7)/10; //0.7-1.3
                hunterStartTime = System.nanoTime();
                //ljudgrejer f ör hunter
            }

            //add powerups on timer
            long puElapsed = (System.nanoTime() - puStartTime) / 1000000;
            if (puElapsed > (spawnRate * 12000*powerUpVariation)) {
                spawner.spawnPowerUp();
                powerUpVariation = (double)(rand.nextInt((14 - 8) + 1)+8)/10; //0.8-1.4
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
                    boat.get(j).getBitmap().recycle();
                    boat.remove(j);
                    boatSmackMP.start();
                    death();
                    break;
                }
                if (boat.get(j).getY() > HEIGHT * 2) {
                    boatMP.stop();
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

            //TODO check bug with left/right edge
            //Checks for left/right edge of the screen, puts player back at edge
            if (player.getX() <= 15) {
                player.setTooFarLeft(true);
                player.setX(15);
            }
            if (player.getX() >= WIDTH - 160) {
                player.setTooFarRight(true);
                player.setX(WIDTH - 160);
                //Is -59 ok? Or is it not scaleable
            }
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            //Collision points. See RockCollision image.
            if (b instanceof Rock) {
                ((Rock) b).collisionPoints();
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
                ((Boat) b).collisionPoints();
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
        player.setIsDead(true);
        player.setLifes((player.getLifes() - 1));
        player.setScoreMultiplyer(1);
        if(player.getAbstinens()>=20) {
            player.setAbstinens(player.getAbstinens() - 20);
        } else {
            player.setAbstinens(0);
        }
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
            String finalScore = (int)player.getScore()+"";
            act.gameOver(finalScore);

        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                death.stop();
                player.setIsDead(false);
            }
        }, 3000);
        //player.setSpritesheet(BitmapFactory.decodeResource(getResources(), R.drawable.croc));
//        warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
//        boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
//        weedPUMP = MediaPlayer.create(this.getContext(), R.raw.weedpickup);
//        vodkaPUMP = MediaPlayer.create(this.getContext(), R.raw.vodkapickup);
//        weedMP = MediaPlayer.create(this.getContext(), R.raw.weed);
//        vodkaMP = MediaPlayer.create(this.getContext(), R.raw.vodka);
//        lsdMP = MediaPlayer.create(this.getContext(), R.raw.lsd);
//        boatSmackMP = MediaPlayer.create(this.getContext(), R.raw.boatsmack);
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
        if(!isInEditMode()) {
            System.out.println("Drawing...");
            final float scaleFactorX = getWidth() / (WIDTH * 1.f);
            final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
            if (canvas != null) {
                final int savedState = canvas.save();
                canvas.scale(scaleFactorX, scaleFactorY);
                Paint paintOriginal = new Paint();
                paintOriginal.setAntiAlias(true);
                paintOriginal.setFilterBitmap(true);
                LightingColorFilter test = new LightingColorFilter(0xFFFFFFFF, 0x00FF00FF);
                if (player.isLsd()) {
                    paintOriginal.setColorFilter(test);
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
                if(go) {
                    canvas.drawText(timerCurrent, WIDTH / 2 - 100, HEIGHT / 2, paintTimer);
                }
                canvas.restoreToCount(savedState);
            }
        }
    }


    public boolean isPaused(){
        if(paused) {
            return true;
        }
        return false;
    }
    public void setActivity(Game act) {
        this.act = act;
    }

    public void pause(){
        System.out.println("Pausing");
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
        sounds.add(warningMP);
        sounds.add(boatMP);
        sounds.add(weedMP);
        sounds.add(weedPUMP);
        sounds.add(vodkaMP);
        sounds.add(vodkaPUMP);
        sounds.add(death);
        sounds.add(breakMP);
        sounds.add(pickupMP);
        sounds.add(smackMP);
        sounds.add(boatSmackMP);
        sounds.add(clickMP);
        sounds.add(music);
        sounds.add(lsdMP);
        if(gameStarted) {
            for(MediaPlayer mp: sounds) {
                System.out.println("ohh");
                if(mp!=null) {
                    System.out.println("KOLLA NU HAHA: "+mp);
                    if (mp.isPlaying()) {
                        System.out.println("KOLLA HAHA: "+mp);
                        mp.pause();
                        soundsToStart.add(mp);
                    }
                }
            }
        }
        pauseStartTime=System.nanoTime();
        paused=true;
    }
    public void resume(){
        System.out.println("Resuming");
        if(!thread.isRunning() && gameStarted) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
        if(gameStarted) {
            for(MediaPlayer mp: soundsToStart) {
                mp.start();
            }
        }
        soundsToStart.clear();
        long pauseElapsed = System.nanoTime() - pauseStartTime;

        rockStartTime+=pauseElapsed;
        boatStartTime+=pauseElapsed;
        hunterStartTime+=pauseElapsed;
        puStartTime+=pauseElapsed;
        for(Hunter h:hunter){
            h.setSpearStartTime(h.getSpearStartTime()-pauseElapsed);
        }
        player.setScoreStartTime(player.getScoreStartTime()-pauseElapsed);
        player.setAbsStartTime(player.getAbsStartTime() - pauseElapsed);
        player.setStartTime(player.getStartTime()-pauseElapsed);
        paused=false;
    }
}