package classes.it.hdp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import classes.it.hdp.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String user;
    ImageView imgLogo,imgLogoText;
    Animation fromTop, fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imgLogo = findViewById(R.id.img_logo);
        imgLogoText = findViewById(R.id.img_logo_text);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        imgLogo.setAnimation(fromTop);
        imgLogoText.setAnimation(fromBottom);


        int SPLASH_SCREEN_TIME_OUT = 2500;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                user = sharedPreferences.getString("username", null);
                if (user != null) {
                    startActivity(new Intent(SplashScreenActivity.this, MPINActivity.class));
                } else {
                    /*Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);*/
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    Pair[] pairs=new Pair[2];
                    pairs[0] =new Pair<View,String>(imgLogo,"logo_image");
                    pairs[1] =new Pair<View,String>(imgLogoText,"logo_image_text");

                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this,
                            pairs);
                    startActivity(intent,options.toBundle());
                }
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}