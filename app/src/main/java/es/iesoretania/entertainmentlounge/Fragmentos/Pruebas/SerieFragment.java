package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveTemporadaSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class SerieFragment extends Fragment {
    TextView tvSerieNombre, tvSerieGenero, tvSerieDescripcion, tvSerieNumTemporadas;
    Button btnSerieSave;
    String key;
    FirebaseFirestore db;
    Serie serie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serie, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        tvSerieNombre = view.findViewById(R.id.tvSerieNombre);
        tvSerieGenero = view.findViewById(R.id.tvSerieGenero);
        tvSerieDescripcion = view.findViewById(R.id.tvSerieDescripcion);
        tvSerieNumTemporadas = view.findViewById(R.id.tvSerieNumTemporadas);
        btnSerieSave = view.findViewById(R.id.btnSerieSave);
        if (getArguments() != null) {
            SerieFragmentArgs args = SerieFragmentArgs.fromBundle(getArguments());
            key = args.getKey();
            db.collection("series").whereEqualTo("id_serie", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        serie = dn.toObject(Serie.class);
                        tvSerieNombre.setText(serie.getNombre());
                        tvSerieGenero.setText(serie.getGenero());
                        tvSerieDescripcion.setText(serie.getDescripcion());
                        tvSerieNumTemporadas.setText("Número de temporadas: " + serie.getTemporadas().size() + "\nNúmero de capítulos temporada 1: " + serie.getTemporadas().get(0).getCapitulos().size());
                    }
                }
            });
        }
        // -- INSERT CODE TO SAVE SERIE -- //
        // VALE ESTO FUNCIONA VAMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOS
        // Cositas que tengo que hacer:
        // - Comprobar que el usuario ya ha guardado la serie para que no pueda volver a guardarla, si no que cuando entre a ver la serie le salga como guardada
        // - Si el usuario deja de seguir la serie, borrar el documento respectivo a la serie del usuario
        btnSerieSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("series").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot dn = task.getResult();
                        serie = dn.toObject(Serie.class);
                        SaveSerie saveSerie = new SaveSerie();
                        saveSerie.setId_serie(key);
                        for (Temporada temp : serie.getTemporadas()) {
                            SaveTemporadaSerie saveTemporadaSerie = new SaveTemporadaSerie();
                            for (int i = 0; i < temp.getCapitulos().size(); i++) {
                                saveTemporadaSerie.getCapitulos_vistos().add(0);
                                saveTemporadaSerie.getCapitulos_puntuacion().add(0.0);
                            }
                            saveSerie.getTemporadas().add(saveTemporadaSerie);
                        }
                        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").add(saveSerie);
                    }
                });
            }
        });
    }
}