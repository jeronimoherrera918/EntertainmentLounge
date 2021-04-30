package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.GridSerieAdapter;
import es.iesoretania.entertainmentlounge.Adapters.SerieAdapter;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class VerSeriesFragment extends Fragment {
    ListView lvListaSeries;
    GridView gvListaSeries;

    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        lvListaSeries = view.findViewById(R.id.lvListaSeries);
        gvListaSeries = view.findViewById(R.id.gvListaSeries);

        // Obtenemos instancia de Realtime DB
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("series");

        // Leemos todos los datos que haya en "series"
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Serie> listaSeries = new ArrayList<>();
                List<String> listaSeriesKeys = new ArrayList<>();

                for (DataSnapshot dn : snapshot.getChildren()) {
                    Serie serie = dn.getValue(Serie.class);
                    listaSeries.add(serie);
                    listaSeriesKeys.add(dn.getKey());
                }

                // LIST VIEW
                SerieAdapter serieAdapter = new SerieAdapter(lvListaSeries.getContext(), R.layout.adapter_series, listaSeries);
                lvListaSeries.setAdapter(serieAdapter);
                lvListaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Navigation.findNavController(view).navigate(VerSeriesFragmentDirections.actionNavVerSeriesToSerieFragment(listaSeriesKeys.get(position)));
                    }
                });

                // GRID VIEW
                GridSerieAdapter gridSerieAdapter = new GridSerieAdapter(gvListaSeries.getContext(), R.layout.adapter_grid_series, listaSeries);
                gvListaSeries.setAdapter(gridSerieAdapter);
                gvListaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Navigation.findNavController(view).navigate(VerSeriesFragmentDirections.actionNavVerSeriesToSerieFragment(listaSeriesKeys.get(position)));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}