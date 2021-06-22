package es.iesoretania.entertainmentlounge.Fragmentos.serie;

import android.graphics.Color;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.iesoretania.entertainmentlounge.Adapters.TemporadasSerieAdapter;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveTemporadaSerie;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Fragmentos.serie.SerieFragmentArgs;
import es.iesoretania.entertainmentlounge.Fragmentos.serie.SerieFragmentDirections;
import es.iesoretania.entertainmentlounge.R;

public class SerieFragment extends Fragment {
    private TextView tvSerieNombre, tvSerieGenero, tvSerieDescripcion, tvSeriePlataformas;
    private Button btnSerieSave;
    private ImageView imgSerieCabecera;
    private ListView listaTemporadas;
    private RatingBar rbPuntuacionSerie;
    private String key;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private Serie serie;

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
        tvSeriePlataformas = view.findViewById(R.id.tvSeriePlataformas);
        btnSerieSave = view.findViewById(R.id.btnSerieSave);
        rbPuntuacionSerie = view.findViewById(R.id.rbPuntuacionSerie);
        imgSerieCabecera = view.findViewById(R.id.imgSerieCabecera);
        listaTemporadas = view.findViewById(R.id.listaTemporadas);
        if (getArguments() != null) {
            SerieFragmentArgs args = SerieFragmentArgs.fromBundle(getArguments());
            key = args.getKey();
            mostrarSerie();
        }
    }

    private void mostrarSerie() {
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("series/" + key + ".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getContext()).load(uri).into(imgSerieCabecera));

        db.collection("series").whereEqualTo("id_serie", key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        serie = dn.toObject(Serie.class);
                        tvSerieNombre.setText(serie.getNombre());
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(serie.getNombre());
                        tvSerieGenero.setText("Género: " + serie.getGenero());
                        tvSerieDescripcion.setText(serie.getDescripcion());
                        String textoPlataformas = "Plataformas:";
                        for (String plat : serie.getPlataformas()) {
                            textoPlataformas = textoPlataformas + " " + plat;
                        }
                        tvSeriePlataformas.setText(textoPlataformas);
                        rbPuntuacionSerie.setRating(Float.parseFloat(serie.getPuntuacion().toString()));
                        adapterListaTemporadas();
                        setupSerie();
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
                Navigation.findNavController(view).navigate(SerieFragmentDirections.actionNavSerieToNavTemporada(serie, serie.getNombre(), position));
            }
        });
    }

    private void setupSerie() {
        adapterListaTemporadas();
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", key).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    estadoBotonGuardar(false);
                } else {
                    estadoBotonGuardar(true);
                }
            }
        });
    }

    private void estadoBotonGuardar(boolean estado) {
        if (!estado) {
            btnSerieSave.setEnabled(false);
            btnSerieSave.setBackgroundColor(Color.GRAY);
        } else {
            btnSerieSave.setEnabled(true);
            activarGuardado();
        }
    }

    private void activarGuardado() {
        btnSerieSave.setBackgroundColor(Color.parseColor("#16618D"));
        btnSerieSave.setOnClickListener(v -> db.collection("series").document(key).get().addOnCompleteListener(guardarSerie -> {
            if (guardarSerie.isSuccessful()) {
                DocumentSnapshot dn = guardarSerie.getResult();
                serie = dn.toObject(Serie.class);
                SaveSerie saveSerie = new SaveSerie();
                saveSerie.setId_serie(key);
                saveSerie.setVistaCompleta(false);
                for (Temporada temp : serie.getTemporadas()) {
                    SaveTemporadaSerie saveTemporadaSerie = new SaveTemporadaSerie();
                    for (int i = 0; i < temp.getCapitulos().size(); i++) {
                        saveTemporadaSerie.getCapitulos_vistos().add(0);
                        saveTemporadaSerie.getCapitulos_puntuacion().add(0.0);
                    }
                    saveSerie.getTemporadas().add(saveTemporadaSerie);
                }
                db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").add(saveSerie);
                btnSerieSave.setEnabled(false);
                btnSerieSave.setBackgroundColor(Color.GRAY);
            }
        }));
    }
}
