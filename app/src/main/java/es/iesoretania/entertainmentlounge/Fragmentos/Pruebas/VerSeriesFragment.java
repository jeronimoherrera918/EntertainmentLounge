package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
    Spinner spFiltros;
    FirebaseFirestore db;
    List<Serie> listaSeries;
    List<String> listaSeriesKeys;

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
        db = FirebaseFirestore.getInstance();
        listRecyclerSeries = view.findViewById(R.id.listRecyclerSeries);
        spFiltros = view.findViewById(R.id.spFiltros);
        List<String> elementosSpinner = new ArrayList<>();
        elementosSpinner.add("Nombre");
        elementosSpinner.add("Genero");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, elementosSpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltros.setAdapter(arrayAdapter);
        spFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarSeriesFiltro(parent.getItemAtPosition(position).toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void mostrarSeriesFiltro(String filtro) {
        db.collection("series").orderBy(filtro).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listaSeries = new ArrayList<>();
                    listaSeriesKeys = new ArrayList<>();
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
                    Log.d("ERROR", task.getException().toString());
                }
            }
        });
    }
}