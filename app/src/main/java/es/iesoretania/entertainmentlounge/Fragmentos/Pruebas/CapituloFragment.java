package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class CapituloFragment extends Fragment {
    RatingBar rbPuntuarCapitulo;
    ImageButton btnMarcarComoVistoCap;

    CapituloFragmentArgs capituloFragmentArgs;
    Serie serie;
    Integer nCapitulo;
    SaveSerie saveSerie;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        }

        comprobacionesIniciales();
    }

    private void comprobacionesIniciales() {
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").whereEqualTo("id_serie", serie.getId_serie()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    saveSerie = task.getResult().getDocuments().get(0).toObject(SaveSerie.class);
                    if (saveSerie.getTemporadas().get(0).getCapitulos_vistos().get(capituloFragmentArgs.getPosition()) == 1) {
                        btnMarcarComoVistoCap.setImageResource(R.drawable.ic_check_true);
                    }
                }
            }
        });
    }
}