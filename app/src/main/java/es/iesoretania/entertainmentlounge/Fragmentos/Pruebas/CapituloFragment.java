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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
    Integer nCapitulo, nCapituloPos, nTemporada;
    SaveSerie saveSerie;
    FirebaseFirestore db;
    Boolean sw = false;
    Double puntuacionTemporadaOld = 0.0;

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

        //region Declaración de elementos del fragmento
        rbPuntuarCapitulo = view.findViewById(R.id.rbPuntuarCapitulo);
        btnMarcarComoVistoCap = view.findViewById(R.id.btnMarcarComoVistoCap);
        btnCapComentar = view.findViewById(R.id.btnCapComentar);
        etCapComentario = view.findViewById(R.id.etCapComentario);
        fabGuardarCambiosCap = view.findViewById(R.id.fabGuardarCambiosCap);
        db = FirebaseFirestore.getInstance();
        rbPuntuarCapitulo.setEnabled(false);
        //endregion

        //region Recoger argumentos
        if (getArguments() != null) {
            capituloFragmentArgs = CapituloFragmentArgs.fromBundle(getArguments());
            serie = capituloFragmentArgs.getSerie();
            nCapitulo = capituloFragmentArgs.getPosition() + 1;
            nCapituloPos = capituloFragmentArgs.getPosition();
            nTemporada = capituloFragmentArgs.getNTemporada();
        }
        //endregion

        comprobacionesIniciales();
    }

    private void comprobacionesIniciales() {
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", serie.getId_serie()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        saveSerie = task.getResult().getDocuments().get(0).toObject(SaveSerie.class);
                        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(nCapituloPos) == 1) {
                            float f = Float.parseFloat(String.valueOf(saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos)));
                            rbPuntuarCapitulo.setRating(f);
                            btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                            rbPuntuarCapitulo.setEnabled(true);
                            btnCapComentar.setEnabled(true);
                        } else {
                            rbPuntuarCapitulo.setEnabled(false);
                            btnCapComentar.setEnabled(false);
                        }
                        for (Double d : saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion()) {
                            puntuacionTemporadaOld += d;
                        }
                        puntuacionTemporadaOld = puntuacionTemporadaOld / serie.getTemporadas().get(nTemporada).getCapitulos().size();
                        activarComentarios();
                        activarPuntuar();
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
                if (!sw) {
                    sw = true;
                    fabGuardarCambiosCap.setEnabled(true);
                    activarGuardarCambios();
                }

                if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(nCapituloPos) == 1) {
                    // PONER UNA CONFIRMACIÓN DE SI QUIERE REALMENTE MARCARLO COMO "NO VISTO" //
                    // if(condition){...code...}else{...code...} //
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                    btnMarcarComoVistoCap.startAnimation(animation);
                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(nCapituloPos, 0);
                    btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_false);
                    rbPuntuarCapitulo.setRating(0f);
                    rbPuntuarCapitulo.setEnabled(false);
                    btnCapComentar.setEnabled(false);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_in);
                    btnMarcarComoVistoCap.startAnimation(animation);
                    saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().set(nCapituloPos, 1);
                    btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                    rbPuntuarCapitulo.setEnabled(true);
                    btnCapComentar.setEnabled(true);
                }
            }
        });
    }

    private void mostrarComentarios() {
        List<Comentario> listaComentarios = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getListaComentarios();
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
                serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getListaComentarios().add(comentario);
                db.collection("series").document(serie.getId_serie()).set(serie);
            }
        });
    }

    private void activarPuntuar() {
        rbPuntuarCapitulo.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!sw) {
                    sw = true;
                    fabGuardarCambiosCap.setEnabled(true);
                    activarGuardarCambios();
                }
            }
        });
    }

    private void activarGuardarCambios() {
        fabGuardarCambiosCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });
    }

    private void guardarCambios() {
        // Si es la primera vez que puntúa un capítulo
        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos) == 0 && rbPuntuarCapitulo.getRating() > 0f) {
            int nVotosActuales = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getnVotos() + 1;
            double puntuacionActual = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getPuntuacionTotal() + (double) rbPuntuarCapitulo.getRating();
            serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setnVotos(nVotosActuales);
            serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacionTotal(puntuacionActual);
            serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacion(puntuacionActual / nVotosActuales);
            saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().set(nCapituloPos, (double) rbPuntuarCapitulo.getRating());
        }

        // Si ya lo ha puntuado anteriormente
        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos) != 0 && rbPuntuarCapitulo.getRating() > 0f) {
            int nVotosActuales = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getnVotos();
            double puntuacionActual = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getPuntuacionTotal() - saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos) + (double) rbPuntuarCapitulo.getRating();
            serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacionTotal(puntuacionActual);
            serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacion(puntuacionActual / nVotosActuales);
            saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().set(nCapituloPos, (double) rbPuntuarCapitulo.getRating());
        }

        // Si lo desmarca y lo actualiza a "no visto"
        if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(nCapituloPos) == 0) {
            if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos) != 0) {
                int nVotosActuales = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getnVotos() - 1;
                double puntuacionActual = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).getPuntuacionTotal() - saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().get(nCapituloPos);
                serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setnVotos(nVotosActuales);
                if (nVotosActuales == 0) {
                    serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacion(0.0);
                    serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacionTotal(0.0);
                } else {
                    serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacion(puntuacionActual / nVotosActuales);
                    serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapituloPos).setPuntuacionTotal(puntuacionActual);
                }

            }
            saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().set(nCapituloPos, 0.0);
        }

        // Actualizamos
        db.collection("series").document(serie.getId_serie()).set(serie).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", serie.getId_serie()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").document(task.getResult().getDocuments().get(0).getId()).set(saveSerie).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Snackbar.make(getView(), "Cambios guardados correctamente", Snackbar.LENGTH_SHORT).show();
                                        actualizarPuntuacionTemporada();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        sw = false;
        fabGuardarCambiosCap.setEnabled(false);
    }

    private void actualizarPuntuacionTemporada() {
        db.collection("series").document(serie.getId_serie()).get().addOnCompleteListener(buscarSerie -> {
            if (buscarSerie.isSuccessful()) {
                Serie infoSerie = buscarSerie.getResult().toObject(Serie.class);
                if (!saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().contains(0.0) && !saveSerie.getTemporadas().get(nTemporada).isVistaCompleta()) {
                    saveSerie.getTemporadas().get(nTemporada).setVistaCompleta(true);
                    infoSerie.getTemporadas().get(nTemporada).setnVotos(infoSerie.getTemporadas().get(nTemporada).getnVotos() + 1);
                    double puntuacionTemporadaPersonal = 0.0;
                    for (Double d : saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion()) {
                        puntuacionTemporadaPersonal += d;
                    }
                    puntuacionTemporadaPersonal = puntuacionTemporadaPersonal / infoSerie.getTemporadas().get(nTemporada).getCapitulos().size();

                    infoSerie.getTemporadas().get(nTemporada).setPuntuacionTotal(infoSerie.getTemporadas().get(nTemporada).getPuntuacionTotal() + puntuacionTemporadaPersonal);
                    infoSerie.getTemporadas().get(nTemporada).setPuntuacion(infoSerie.getTemporadas().get(nTemporada).getPuntuacionTotal() / infoSerie.getTemporadas().get(nTemporada).getnVotos());
                } else if (saveSerie.getTemporadas().get(nTemporada).isVistaCompleta() && !saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion().contains(0.0)) {
                    double puntuacionTemporadaPersonal = 0.0;
                    for (Double d : saveSerie.getTemporadas().get(nTemporada).getCapitulos_puntuacion()) {
                        puntuacionTemporadaPersonal += d;
                    }
                    puntuacionTemporadaPersonal = puntuacionTemporadaPersonal / infoSerie.getTemporadas().get(nTemporada).getCapitulos().size();

                    infoSerie.getTemporadas().get(nTemporada).setPuntuacionTotal(infoSerie.getTemporadas().get(nTemporada).getPuntuacionTotal() + puntuacionTemporadaPersonal - puntuacionTemporadaOld);
                    infoSerie.getTemporadas().get(nTemporada).setPuntuacion(infoSerie.getTemporadas().get(nTemporada).getPuntuacionTotal() / infoSerie.getTemporadas().get(nTemporada).getnVotos());

                    puntuacionTemporadaOld = puntuacionTemporadaPersonal;
                }

                db.collection("series").document(buscarSerie.getResult().getId()).set(infoSerie).addOnCompleteListener(updateSerie -> {
                    if (updateSerie.isSuccessful()) {
                        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", saveSerie.getId_serie()).get().addOnCompleteListener(updateSaveSerie -> {
                            if (updateSaveSerie.isSuccessful()) {
                                db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").document(updateSaveSerie.getResult().getDocuments().get(0).getId()).set(saveSerie);
                            }
                        });
                    }
                });
            }
        });
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