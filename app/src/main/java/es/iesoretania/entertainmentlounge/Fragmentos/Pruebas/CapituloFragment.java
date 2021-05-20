package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerComentarios;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class CapituloFragment extends Fragment {
    RatingBar rbPuntuarCapitulo;
    ImageButton btnMarcarComoVistoCap;
    RecyclerView listRecyclerComentarios;
    Button btnCapComentar;
    EditText etCapComentario;
    FloatingActionButton fabGuardarCambiosCap;
    CapituloFragmentArgs capituloFragmentArgs;
    Serie serie;
    Integer nCapitulo, nTemporada;
    SaveSerie saveSerie;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capitulo, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rbPuntuarCapitulo = view.findViewById(R.id.rbPuntuarCapitulo);
        btnMarcarComoVistoCap = view.findViewById(R.id.btnMarcarComoVistoCap);
        btnCapComentar = view.findViewById(R.id.btnCapComentar);
        etCapComentario = view.findViewById(R.id.etCapComentario);
        fabGuardarCambiosCap = view.findViewById(R.id.fabGuardarCambiosCap);
        db = FirebaseFirestore.getInstance();
        rbPuntuarCapitulo.setEnabled(false);
        if (getArguments() != null) {
            capituloFragmentArgs = CapituloFragmentArgs.fromBundle(getArguments());
            serie = capituloFragmentArgs.getSerie();
            nCapitulo = capituloFragmentArgs.getPosition() + 1;
            nTemporada = capituloFragmentArgs.getNTemporada();
        }
        comprobacionesIniciales();
    }

    private void comprobacionesIniciales() {
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", serie.getId_serie()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        saveSerie = task.getResult().getDocuments().get(0).toObject(SaveSerie.class);
                        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(capituloFragmentArgs.getPosition()) == 1) {
                            btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                            activarComentarios();
                            activarPuntuar();
                        }
                        activarGuardarCapitulo();
                    }
                    mostrarComentarios();
                }
            }
        });
    }

    private void activarGuardarCapitulo() {
        btnMarcarComoVistoCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(nCapitulo) == 1) {
                    // PONER UNA CONFIRMACIÓN DE SI QUIERE REALMENTE MARCARLO COMO "NO VISTO"
                    // if(condition){...code...}else{...code...} //
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                    btnMarcarComoVistoCap.startAnimation(animation);
                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(nCapitulo, 0);
                    btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_false);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                    btnMarcarComoVistoCap.startAnimation(animation);
                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(nCapitulo, 1);
                    btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                }
            }
        });
    }

    private void mostrarComentarios() {
        List<Comentario> listaComentarios = serie.getTemporadas().get(nTemporada).getCapitulos().get(capituloFragmentArgs.getPosition()).getListaComentarios();
        RecyclerComentarios recyclerComentarios = new RecyclerComentarios(listaComentarios, getContext());
        listRecyclerComentarios = this.getView().findViewById(R.id.listRecyclerComentarios);
        listRecyclerComentarios.setHasFixedSize(true);
        listRecyclerComentarios.setLayoutManager(new LinearLayoutManager(listRecyclerComentarios.getContext()));
        listRecyclerComentarios.setAdapter(recyclerComentarios);
    }

    private void activarComentarios() {
        btnCapComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comentario comentario = new Comentario(etCapComentario.getText().toString(), UserData.ID_USER_DB, generarID(6));
                serie.getTemporadas().get(nTemporada).getCapitulos().get(capituloFragmentArgs.getPosition()).getListaComentarios().add(comentario);
                db.collection("series").document(serie.getId_serie()).set(serie);
            }
        });
    }

    private void activarPuntuar() {
        rbPuntuarCapitulo.setEnabled(true);
        rbPuntuarCapitulo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 0.5f) {
                    ratingBar.setRating(0.5f);
                    Snackbar.make(rbPuntuarCapitulo.getRootView(), "Si vas a votar, debe ser mayor a cero", Snackbar.LENGTH_SHORT).show();
                }
                Double d = Double.valueOf(rating);
                saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().set(capituloFragmentArgs.getPosition(), d);
                // System.out.println(saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(capituloFragmentArgs.getPosition()));
            }
        });
    }

    private void guardarCambios() {
        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(capituloFragmentArgs.getPosition()) == 0) {
            // serie.getTemporadas().get(nTemporada).getCapitulos().get(capituloFragmentArgs.getPosition()).setnVotos(getNVotos() + 1);
            // Aquí calcular la media pero madre mía esto va a ser un locurón
            // Hacer una función eficiente para calcular la media (recoger el valor actual + los números de votos + sumarle la nota actual del usuario)
        } else {

        }
    }

    private String generarID(int tam) {
        String key = "";
        Random random = new Random();
        for (int i = 0; i < tam; i++) {
            char randomizedCharacter_1 = (char) (random.nextInt(26) + 'A');
            key += randomizedCharacter_1;
            char randomizedCharacter_2 = (char) (random.nextInt(26) + 'a');
            key += randomizedCharacter_2;
        }
        return key;
    }
}