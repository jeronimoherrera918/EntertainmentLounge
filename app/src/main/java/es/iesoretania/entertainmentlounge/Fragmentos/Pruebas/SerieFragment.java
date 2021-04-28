package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class SerieFragment extends Fragment {
    TextView tvSerieNombre, tvSerieGenero, tvSerieDescripcion, tvSerieNumTemporadas;

    String key;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ValueEventListener leerSerie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serie, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myRef.child(key).removeEventListener(leerSerie);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Log.d("DATA", getActivity().getTitle().toString()); <-- Título de la actividad principal (D/DATA: EntertainmentLounge)

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("series");

        tvSerieNombre = view.findViewById(R.id.tvSerieNombre);
        tvSerieGenero = view.findViewById(R.id.tvSerieGenero);
        tvSerieDescripcion = view.findViewById(R.id.tvSerieDescripcion);
        tvSerieNumTemporadas = view.findViewById(R.id.tvSerieNumTemporadas);

        if (getArguments() != null) {
            SerieFragmentArgs args = SerieFragmentArgs.fromBundle(getArguments());
            key = args.getKey();
            leerSerie = leerSerie(key);
            myRef.child(key).addValueEventListener(leerSerie);
        }
    }

    public ValueEventListener leerSerie(String key) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Serie serie = snapshot.getValue(Serie.class);
                if (serie != null) {
                    tvSerieNombre.setText(serie.getNombre());
                    tvSerieGenero.setText(serie.getGenero());
                    tvSerieDescripcion.setText(serie.getDescripcion());
                    tvSerieNumTemporadas.setText("Número de temporadas: " + String.valueOf(serie.getTemporadas().size()));
                    Toast.makeText(getContext(), "Nº Caps. Temp. 1: [" + serie.getTemporadas().get(0).getCapitulos().size() + "]", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }
}