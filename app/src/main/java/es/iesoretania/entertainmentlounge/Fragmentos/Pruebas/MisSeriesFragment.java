package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerSeries;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class MisSeriesFragment extends Fragment {
    RecyclerView recyclerMisSeries;
    Spinner spFiltrosMisSeries;
    FirebaseFirestore db;
    List<Serie> listaSeries;
    List<String> listaSeriesKeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_series, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerMisSeries = view.findViewById(R.id.recyclerMisSeries);
        spFiltrosMisSeries = view.findViewById(R.id.spFiltrosMisSeries);
        List<String> elementosSpinner = new ArrayList<>();
        elementosSpinner.add("Nombre");
        elementosSpinner.add("Genero");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, elementosSpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltrosMisSeries.setAdapter(arrayAdapter);

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listaSeriesKeys = new ArrayList<>();
                    for (QueryDocumentSnapshot dn : task.getResult()) {
                        SaveSerie saveSerie = dn.toObject(SaveSerie.class);
                        listaSeriesKeys.add(saveSerie.getId_serie());
                    }
                    db.collection("series").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                listaSeries = new ArrayList<>();
                                for (QueryDocumentSnapshot dn : task.getResult()) {
                                    if (listaSeriesKeys.contains(dn.getId())) {
                                        listaSeries.add(dn.toObject(Serie.class));
                                    }
                                }

                                RecyclerSeries recyclerSeries = new RecyclerSeries(listaSeries, getContext());
                                recyclerSeries.setOnItemClickListener((position, v) -> Navigation.findNavController(v).navigate(MisSeriesFragmentDirections.actionNavMisSeriesToNavSerie(listaSeries.get(position).getId_serie())));
                                recyclerMisSeries.setHasFixedSize(true);
                                recyclerMisSeries.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerMisSeries.setAdapter(recyclerSeries);
                            }
                        }
                    });
                }
            }
        });
    }
}