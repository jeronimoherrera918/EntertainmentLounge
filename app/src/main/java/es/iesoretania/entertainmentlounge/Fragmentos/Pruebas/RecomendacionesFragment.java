package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class RecomendacionesFragment extends Fragment {
    private FirebaseFirestore db;
    private EditText etRecomendaciones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recomendaciones, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        etRecomendaciones = view.findViewById(R.id.etRecomendaciones);

        // ESTRUCTURA DE LOS SELECT //
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(seriesGuardadas -> {
            if (seriesGuardadas.isSuccessful()) {
                if (seriesGuardadas.getResult().getDocuments().size() >= 5) {
                    // COMENZAR RECOMENDACIONES //
                    List<SaveSerie> listaSeries = new ArrayList<>();
                    for (DocumentSnapshot dn : seriesGuardadas.getResult()) {
                        listaSeries.add(dn.toObject(SaveSerie.class));
                    }

                } else {
                    System.out.println("El usuario no tiene suficientes series para empezar a recomendarle otras");
                }
            }
        });
    }
}