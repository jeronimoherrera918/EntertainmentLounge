package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class SerieFragment extends Fragment {
    TextView tvSerieNombre, tvSerieGenero, tvSerieDescripcion;

    String key;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serie, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("series").child(key);

        tvSerieNombre = view.findViewById(R.id.tvSerieNombre);
        tvSerieGenero = view.findViewById(R.id.tvSerieGenero);
        tvSerieDescripcion = view.findViewById(R.id.tvSerieDescripcion);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Serie serie = snapshot.getValue(Serie.class);
                if (serie != null) {
                    // PONER AQUI LOS TEXTVIEW
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}