package fi.orb.orb_001;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {

    int height;
    SharedPreferences mSharedPreferences;
    private static final String PREFS = "prefs";
    private static final String HEIGHT = "height";



    private static String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);


        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.splash);

        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(2500);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            // Start main activity
            height = findViewById(R.id.splash_height).getHeight();

            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putInt(HEIGHT, height);
            e.apply();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}