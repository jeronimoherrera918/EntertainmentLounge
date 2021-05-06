package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.GridSerieAdapter;
import es.iesoretania.entertainmentlounge.Adapters.SerieAdapter;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class VerSeriesFragment extends Fragment {
    ListView lvListaSeries;
    GridView gvListaSeries;
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
        lvListaSeries = view.findViewById(R.id.lvListaSeries);
        gvListaSeries = view.findViewById(R.id.gvListaSeries);

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
                    SerieAdapter serieAdapter = new SerieAdapter(lvListaSeries.getContext(), R.layout.adapter_series, listaSeries);
                    lvListaSeries.setAdapter(serieAdapter);
                    lvListaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Navigation.findNavController(view).navigate(VerSeriesFragmentDirections.actionNavVerSeriesToSerieFragment(listaSeriesKeys.get(position)));
                        }
                    });
                    GridSerieAdapter gridSerieAdapter = new GridSerieAdapter(gvListaSeries.getContext(), R.layout.adapter_grid_series, listaSeries);
                    gvListaSeries.setAdapter(gridSerieAdapter);
                    gvListaSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Navigation.findNavController(view).navigate(VerSeriesFragmentDirections.actionNavVerSeriesToSerieFragment(listaSeriesKeys.get(position)));
                        }
                    });
                } else {
                    //-- ERROR HERE --//
                }
            }
        });
    }
}