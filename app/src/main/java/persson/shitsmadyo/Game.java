package persson.shitsmadyo;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Game extends FragmentActivity {
    GamePanel gamePanel;
    FragmentManager fragmentManager;
    MediaPlayer clickMP;
    TextView textScore, textMulti, textAbs, textLifes, textPause, gameOver, scoreOver, overScore;
    String currentScore, currentMulti, currentAbs, currentLifes, finalScore;
    Button textResume, buttonExit, buttonOptions, buttonOverAgain, buttonExitOver;
    gamePause fragment;
    GameOver fragmentOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        gamePanel=(GamePanel)findViewById(R.id.gamepanel);
        gamePanel.setActivity(this);
        fragmentManager = getSupportFragmentManager();

        clickMP = MediaPlayer.create(findViewById(R.id.activity_game).getContext(), R.raw.click);

        fragment= (gamePause)fragmentManager.findFragmentById(R.id.gamepausefragment);
        fragmentOver = (GameOver)fragmentManager.findFragmentById(R.id.gameoverfragment);
        textScore = (TextView)findViewById(R.id.scorer);
        textMulti = (TextView)findViewById(R.id.scorem);
        textAbs = (TextView)findViewById(R.id.abs);
        textLifes = (TextView)findViewById(R.id.lifes);
        textResume=(Button)findViewById(R.id.pauseresume);
        buttonExit=(Button)findViewById(R.id.pauseexit);
        buttonOptions=(Button)findViewById(R.id.pauseoptions);
        textPause=(TextView)findViewById(R.id.pauseText);
        buttonOverAgain=(Button)findViewById(R.id.overrestart);
        buttonExitOver=(Button)findViewById(R.id.overexit);
        gameOver=(TextView)findViewById(R.id.overText);
        overScore=(TextView)findViewById(R.id.overScore);
        scoreOver=(TextView)findViewById(R.id.scoreOver);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/DJGROSS.ttf");
        Typeface font3 = Typeface.createFromAsset(getAssets(), "fonts/justice.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/justiceout.ttf");
        textScore.setTypeface(font3);
        textMulti.setTypeface(font3);
        textAbs.setTypeface(font3);
        textLifes.setTypeface(font3);
        textResume.setTypeface(font2);
        buttonExit.setTypeface(font2);
        buttonOptions.setTypeface(font2);
        textPause.setTypeface(font3);
        buttonExitOver.setTypeface(font2);
        buttonOverAgain.setTypeface(font2);
        gameOver.setTypeface(font3);
        scoreOver.setTypeface(font);
        overScore.setTypeface(font3);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(fragment);
        ft.commit();
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.hide(fragmentOver);
        ft2.commit();
    }

    public void setScoreText(final String currentScore){
        this.currentScore=currentScore;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(gamePanel.getPlayer().isLsd()) {
                    textScore.setText(currentScore);
                    textScore.setTextColor(Color.MAGENTA);
                }else {
                    textScore.setTextColor(Color.WHITE);
                    textScore.setText(currentScore);
                }
            }
        });
    }

    public void setScoreMultiText(final String currentMulti){
        this.currentMulti=currentMulti;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    textMulti.setText(currentMulti);
            }
        });
    }
    public void gameOver(final String score) {
        this.finalScore=score;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreOver.setText(score);
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
                ft2.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                ft2.show(fragmentOver);
                ft2.commit();
            }
        });
    }

    public void setAbs(final String currentAbs){
        this.currentAbs=currentAbs;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textAbs.setText(currentAbs);
            }
        });
    }

    public void setLifes(final String currentLifes) {
        this.currentLifes=currentLifes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLifes.setText(currentLifes);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        gamePanel.pause();
        if(gamePanel.getPlayer().getPlaying()) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            ft.show(fragment);
            ft.commit();
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        gamePanel.pause();
        if(gamePanel.getPlayer().getPlaying()) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            ft.show(fragment);
            ft.commit();
        }
    }
    public void exit(View view){
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    public void playAgain(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onRestart(){
        super.onResume();
        if(gamePanel.getPlayer().getPlaying()) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            ft.hide(fragment);
            ft.commit();
        }
    }

    public void pauseButton(View view){
        if(gamePanel.getPlayer().getPlaying()) {
            if (gamePanel.isPaused()) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                ft.hide(fragment);
                ft.commit();
                gamePanel.resume();
                clickMP.start();
            } else {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                ft.show(fragment);
                ft.commit();
                gamePanel.pause();
                clickMP.start();
            }
        }
    }
}