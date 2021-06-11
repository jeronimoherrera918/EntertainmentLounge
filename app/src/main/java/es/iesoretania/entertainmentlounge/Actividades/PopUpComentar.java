package es.iesoretania.entertainmentlounge.Actividades;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import es.iesoretania.entertainmentlounge.R;

public class PopUpComentar extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_comentar);

        DisplayMetrics ventana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ventana);

        int ancho = ventana.widthPixels;
        int alto = ventana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.85), (int) (alto * 0.5));
    }
}
