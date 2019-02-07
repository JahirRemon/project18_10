package com.example.mdjahirulislam.doobbi.view;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.mdjahirulislam.doobbi.R;

public class SplashActivity extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StartAnimations();
    }
    private void StartAnimations() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
////        ImageView l= findViewById(R.id.lin_lay);
////        Drawable drawable = getResources().getDrawable( R.drawable.boss );
////        l.clearAnimation();
////        l.startAnimation(anim);
////        drawable.setAlpha( 45 );
////        l.setImageDrawable( drawable );
////        l.setScaleType( ImageView.ScaleType.CENTER_CROP );
////        l.setScaleY( 1f );
//
//
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.splash);
//        iv.clearAnimation();
//        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 1000) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }
}
