package es.iesoretania.entertainmentlounge.Fragmentos.serie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerSeries;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Fragmentos.serie.VerSeriesFragmentDirections;
import es.iesoretania.entertainmentlounge.R;

public class VerSeriesFragment extends Fragment {
    private RecyclerView listRecyclerSeries;
    private Spinner spFiltros;
    private EditText etBusquedaSerie;
    private FloatingActionButton fabBusquedaSerie;
    private FirebaseFirestore db;
    private List<Serie> listaSeries = new ArrayList<>();
    private List<String> listaSeriesKeys = new ArrayList<>();

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
        etBusquedaSerie = view.findViewById(R.id.etBusquedaSerie);
        fabBusquedaSerie = view.findViewById(R.id.fabBusquedaSerie);
        spFiltros = view.findViewById(R.id.spFiltros);
        List<String> elementosSpinner = new ArrayList<>();
        elementosSpinner.add("Nombre");
        elementosSpinner.add("Genero");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, elementosSpinner);
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

        fabBusquedaSerie.setOnClickListener(v -> {
            if (!etBusquedaSerie.getText().toString().equals("")) {
                buscarSerie(etBusquedaSerie.getText().toString());
            }
        });
    }

    private void mostrarSeriesFiltro(String filtro) {
        listaSeries.clear();
        listaSeriesKeys.clear();
        db.collection("series").orderBy(filtro).get().addOnCompleteListener(mostrarSeries -> {
            if (mostrarSeries.isSuccessful()) {
                for (QueryDocumentSnapshot dn : mostrarSeries.getResult()) {
                    Serie serie = dn.toObject(Serie.class);
                    listaSeries.add(serie);
                    listaSeriesKeys.add(dn.getId());
                }
                setRecyclerViewSeries();
            } else {
                Log.d("ERROR", mostrarSeries.getException().toString());
            }
        });
    }

    private void buscarSerie(String busq) {
        listaSeries.clear();
        listaSeriesKeys.clear();

        db.collection("series").whereGreaterThanOrEqualTo("nombre", busq).whereLessThan("nombre", busq + 'z').get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot dn : task.getResult()) {
                    listaSeries.add(dn.toObject(Serie.class));
                    listaSeriesKeys.add(dn.toObject(Serie.class).getId_serie());
                }
                setRecyclerViewSeries();
            }
        });
    }

    private void setRecyclerViewSeries() {
        RecyclerSeries recyclerSeries = new RecyclerSeries(listaSeries, listRecyclerSeries.getContext());
        recyclerSeries.setOnItemClickListener((position, v) -> Navigation.findNavController(v).navigate(VerSeriesFragmentDirections.actionNavVerSeriesToSerieFragment(listaSeriesKeys.get(position))));
        listRecyclerSeries.setHasFixedSize(true);
        listRecyclerSeries.setLayoutManager(new LinearLayoutManager(listRecyclerSeries.getContext()));
        listRecyclerSeries.setAdapter(recyclerSeries);
    }
}