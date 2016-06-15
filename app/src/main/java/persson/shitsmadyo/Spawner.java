package persson.shitsmadyo;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pontus on 2016-06-15.
 */
public class Spawner {
    private Random rand = new Random();
    GamePanel gamePanel;

    public Spawner(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void spawnRock() {
        ArrayList<GameObject> rock = gamePanel.getRockList();
        boolean rockVar = rand.nextBoolean();
        int pos;
        if (rockVar) {
            pos=collisionSpawn(194, 286);
            rock.add(new Rock(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.rock), pos, -250, 194, 286, (int) gamePanel.getPlayer().getTime(), 7));
        } else {
            pos=collisionSpawn(289, 139);
            rock.add(new RockOne(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.rockone), pos, -250, 289, 139, (int) gamePanel.getPlayer().getTime(), 7));
        }
        //reset timer
        // gamePanel.setRock(rock);
    }

    public void spawnBoat(){
        //      final int pos = (int) (rand.nextDouble() * (WIDTH - 262));
        //   warning.add(new Warning(BitmapFactory.decodeResource(getResources(), R.drawable.warning), pos + 70, 0, 150, 150, (int) player.getTime(), 3));
        //  System.out.println("Creating warning");
        //MediaPlayer warningMP = MediaPlayer.create(this.getContext(), R.raw.boatwarning);
        //final MediaPlayer boatMP = MediaPlayer.create(this.getContext(), R.raw.boatsound);
        MediaPlayer warningMP = gamePanel.getWarningMP();
        MediaPlayer boatMP = gamePanel.getBoatMP();
        warningMP = MediaPlayer.create(gamePanel.getContext(), R.raw.boatwarning);
        warningMP.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MediaPlayer boatMP = MediaPlayer.create(gamePanel.getContext(), R.raw.boatsound);
                ArrayList<Boat> boat = gamePanel.getBoat();
                int pos = collisionSpawn(168, 410);
                int boatVar = rand.nextInt(3);
                if (boatVar == 0) {
                    boat.add(new Boat(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.boatnatural), pos, -600, 168, 410, (int) gamePanel.getPlayer().getTime(), 3));
                } else if (boatVar == 1) {
                    boat.add(new Boat(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.boatcoke), pos, -600, 168, 410, (int) gamePanel.getPlayer().getTime(), 3));
                } else if (boatVar == 2) {
                    boat.add(new Boat(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.boathoe), pos, -600, 168, 410, (int) gamePanel.getPlayer().getTime(), 3));
                }
                //warning.remove(0);
                boatMP.start();
            }
        }, 1500);
        //warningMP.release();
    }

    public void spawnHunter(){
        ArrayList<Hunter> hunter = gamePanel.getHunter();
        int pos = collisionSpawn(125, 125);
        hunter.add(new Hunter(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.hunter), pos, -300, 125, 125, 1));
    }

    public void spawnPowerUp(){
        ArrayList<PowerUp> powerup = gamePanel.getPowerup();
        int pos = collisionSpawn(75, 75);
        int powerVar = rand.nextInt(4);
        if (powerVar == 1) {
            powerup.add(new Weed(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.weed), pos, 0, 100, 100, (int) gamePanel.getPlayer().getTime(), 8));
        } else if (powerVar == 2) {
            powerup.add(new Vodka(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.vodka), pos, 0, 75, 75, (int) gamePanel.getPlayer().getTime(), 1));
        } else if (powerVar == 3) {
            powerup.add(new Lsd(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.lsd), pos, 0, 75, 75, (int) gamePanel.getPlayer().getTime(), 1));
        }
    }

    public int collisionSpawn(int width, int height){
        boolean correct = false;
        boolean collision;

        ArrayList<GameObject> objects = new ArrayList<GameObject>();
        objects.addAll(gamePanel.getRockList());
        objects.addAll(gamePanel.getHunter());
        objects.addAll(gamePanel.getBoat());
     //   objects.addAll(gamePanel.getPowerup());


        int pos = (int) (rand.nextDouble() * (gamePanel.WIDTH - width));

        Rect rectTemp = new Rect(pos, 0, pos+width, height);
        while(!correct) {
            collision = true;
            for(int i = 0; i<objects.size(); i++) {
                if(collision(rectTemp, objects.get(i))) {
                    collision=false;
                    pos = (int)(rand.nextDouble() * (gamePanel.WIDTH - width));
                    rectTemp = new Rect(pos, 0, pos+width, height);
                }
            }
            if(collision) {
                correct=true;
            }
        }
        return pos;
    }

    public boolean collision(Rect r, GameObject object) {
        if (Rect.intersects(r, object.getRectangle())) {
            return true;
        }
        return false;
    }
}
