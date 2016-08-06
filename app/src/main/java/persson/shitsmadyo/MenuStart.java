package persson.shitsmadyo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MenuStart extends Activity {
    MediaPlayer clickMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button test = (Button)findViewById(R.id.angry_btn);
        Button testb = (Button)findViewById(R.id.angry_btn2);
        Button testc = (Button)findViewById(R.id.angry_btn3);
        TextView test2 = (TextView)findViewById(R.id.junkee);
        TextView test3 = (TextView)findViewById(R.id.junkee2);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/justiceout.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/DJGROSS.ttf");
        Typeface font3 = Typeface.createFromAsset(getAssets(), "fonts/justice.ttf");
        test.setTypeface(font);
        test2.setTypeface(font2);
        test3.setTypeface(font3);
        testb.setTypeface(font);
        testc.setTypeface(font);
        clickMP = MediaPlayer.create(findViewById(R.id.activity_menu).getContext(), R.raw.click);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
    public void startGame(View view) {
        clickMP.start();
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
