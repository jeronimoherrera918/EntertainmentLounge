package es.iesoretania.entertainmentlounge.Actividades;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class PopUpComentar extends AppCompatActivity {
    EditText etComentarComentario;
    Button btnComentarPublicar;
    FirebaseFirestore db;
    Serie serie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_comentar);

        etComentarComentario = findViewById(R.id.etComentarComentario);
        btnComentarPublicar = findViewById(R.id.btnComentarPublicar);

        db = FirebaseFirestore.getInstance();

        db.collection("series").document(getIntent().getExtras().getString("keySerie")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    serie = task.getResult().toObject(Serie.class);
                }
            }
        });

        btnComentarPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etComentarComentario.getText().equals("")) {
                    Comentario comentario = new Comentario(etComentarComentario.getText().toString(), UserData.ID_USER_DB, generarID());
                    serie.getTemporadas().get(getIntent().getExtras().getInt("nTemporada")).getCapitulos().get(getIntent().getExtras().getInt("nCapitulo")).getListaComentarios().add(comentario);
                    db.collection("series").document(serie.getId_serie()).set(serie);
                    finish();
                }
            }
        });

        DisplayMetrics ventana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ventana);

        int ancho = ventana.widthPixels;
        int alto = ventana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.85), (int) (alto * 0.5));
    }

    private String generarID() {
        String key = "";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char randomizedCharacter_1 = (char) (random.nextInt(26) + 'A');
            key += randomizedCharacter_1;
            char randomizedCharacter_2 = (char) (random.nextInt(26) + 'a');
            key += randomizedCharacter_2;
        }
        return key;
    }
}
