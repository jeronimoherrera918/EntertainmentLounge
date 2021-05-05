package es.iesoretania.entertainmentlounge.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long time = 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                finish();
            }
        }, time);
    }
}
