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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerSeries;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class VerSeriesFragment extends Fragment {
    RecyclerView listRecyclerSeries;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ver_series, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listRecyclerSeries = view.findViewById(R.id.listRecyclerSeries);

        db = FirebaseFirestore.getInstance();
        db.collection("series").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Serie> listaSeries = new ArrayList<>();
                    List<String> listaSeriesKeys = new ArrayList<>();
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        Serie serie = dn.toObject(Serie.class);
                        listaSeries.add(serie);
                        listaSeriesKeys.add(dn.getId());
                    }
                    RecyclerSeries recyclerSeries = new RecyclerSeries(listaSeries, listRecyclerSeries.getContext());
                    listRecyclerSeries.setHasFixedSize(true);
                    listRecyclerSeries.setLayoutManager(new LinearLayoutManager(listRecyclerSeries.getContext()));
                    listRecyclerSeries.setAdapter(recyclerSeries);
                } else {
                    //-- ERROR HERE --//
                }
            }
        });
    }
}