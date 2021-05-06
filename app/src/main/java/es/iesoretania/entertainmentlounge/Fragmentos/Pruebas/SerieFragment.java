package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.graphics.Color;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Adapters.TemporadasSerieAdapter;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveTemporadaSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class SerieFragment extends Fragment {
    TextView tvSerieNombre, tvSerieGenero, tvSerieDescripcion, tvSerieNumTemporadas;
    Button btnSerieSave, btnComentar;
    EditText etComentario;
    ListView listaTemporadas;
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

        btnComentar = view.findViewById(R.id.btnComentar);
        listaTemporadas = view.findViewById(R.id.listaTemporadas);
        etComentario = view.findViewById(R.id.etComentario);

        if (getArguments() != null) {
            SerieFragmentArgs args = SerieFragmentArgs.fromBundle(getArguments());
            key = args.getKey();
            mostrarSerie();
        }
    }

    private void mostrarSerie() {
        db.collection("series").whereEqualTo("id_serie", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        serie = dn.toObject(Serie.class);
                        tvSerieNombre.setText(serie.getNombre());
                        tvSerieGenero.setText(serie.getGenero());
                        tvSerieDescripcion.setText(serie.getDescripcion());
                        tvSerieNumTemporadas.setText("Número de temporadas: " + serie.getTemporadas().size() + "\nNúmero de capítulos temporada 1: " + serie.getTemporadas().get(0).getCapitulos().size());
                        adapterListaTemporadas();
                        comprobacionSerie();

                    }
                }
            }
        });
    }

    private void adapterListaTemporadas() {
        TemporadasSerieAdapter tempAdapter = new TemporadasSerieAdapter(listaTemporadas.getContext(), R.layout.adapter_temporada, serie.getTemporadas());
        listaTemporadas.setAdapter(tempAdapter);
        listaTemporadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), "Posición: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void comprobacionSerie() {
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        btnSerieSave.setEnabled(false);
                        btnSerieSave.setBackgroundColor(Color.RED);
                        if (task.getResult().getDocuments().get(0) != null) {
                            Toast.makeText(getContext(), "Tienes la serie guardada", Toast.LENGTH_SHORT).show();
                            btnComentar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Entrar en profundidad con esto
                                    Comentario comentario = new Comentario();
                                    comentario.setComentario(etComentario.getText().toString());
                                    comentario.setId_usuario(UserData.ID_USER_DB);
                                    comentario.setNum_likes(0);
                                    db.collection("series").document(key).collection("comentarios").add(comentario);
                                }
                            });
                        }
                    } else {
                        btnSave_noGuardada();
                        Toast.makeText(getContext(), "No tienes la serie guardada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void btnSave_noGuardada() {
        btnSerieSave.setEnabled(true);
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
