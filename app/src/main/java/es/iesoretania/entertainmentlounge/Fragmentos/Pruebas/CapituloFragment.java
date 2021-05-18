package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class CapituloFragment extends Fragment {
    RatingBar rbPuntuarCapitulo;
    ImageButton btnMarcarComoVistoCap;

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
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            capituloFragmentArgs = CapituloFragmentArgs.fromBundle(getArguments());
            serie = capituloFragmentArgs.getSerie();
            System.out.println(serie.getId_serie());
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
                    saveSerie = task.getResult().getDocuments().get(0).toObject(SaveSerie.class);
                    if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(capituloFragmentArgs.getPosition()) == 1) {
                        btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                    }
                    activarGuardarCapitulo();

                }
            }
        });
    }

    private void activarGuardarCapitulo() {
        btnMarcarComoVistoCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveSerie.getTemporadas().get(nTemporada).getCapitulos_vistos().get(nCapitulo) == 1) {
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
        List<Comentario> listaComentarios = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapitulo).getListaComentarios();

    }
}