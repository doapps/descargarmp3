package me.doapps.descargarmp3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Splash extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(Splash.this,Main.class);
                startActivity(i);
                finish();

            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
