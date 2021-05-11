package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class TemporadaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temporada, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: SE PUEDEN USAR ARGUMENTOS PARA HACER EL SETLABEL DEL FRAGMENT
        // TODO: SERÍA ASÍ EN EL XML --> android:label="x{argumento}x"
        if (getArguments() != null) {
            TemporadaFragmentArgs temporadaFragmentArgs = TemporadaFragmentArgs.fromBundle(getArguments());
            Serie serie = temporadaFragmentArgs.getSerie();
            Toast.makeText(getContext(), "Nº Temporadas: " + serie.getTemporadas().size(), Toast.LENGTH_SHORT).show();
        }
    }
}