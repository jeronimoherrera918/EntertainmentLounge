package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerCapitulos;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class TemporadaFragment extends Fragment {
    Serie serie;
    RecyclerView listRecyclerCapitulos;
    FloatingActionButton fabGuardarCambios;
    RecyclerCapitulos recyclerCapitulos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temporada, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listRecyclerCapitulos = view.findViewById(R.id.listRecyclerCapitulos);
        fabGuardarCambios = view.findViewById(R.id.fabGuardarCambios);
        if (getArguments() != null) {
            TemporadaFragmentArgs temporadaFragmentArgs = TemporadaFragmentArgs.fromBundle(getArguments());
            serie = temporadaFragmentArgs.getSerie();
            recyclerCapitulos = new RecyclerCapitulos(serie.getTemporadas().get(temporadaFragmentArgs.getNTemporada()).getCapitulos(), listRecyclerCapitulos.getContext(), serie.getId_serie(), temporadaFragmentArgs.getNTemporada(), fabGuardarCambios);
            recyclerCapitulos.setOnItemClickListener(new RecyclerCapitulos.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Navigation.findNavController(v).navigate(R.id.action_nav_temporada_to_nav_capitulo);
                }
            });
            listRecyclerCapitulos.setHasFixedSize(true);
            listRecyclerCapitulos.setLayoutManager(new LinearLayoutManager(listRecyclerCapitulos.getContext()));
            listRecyclerCapitulos.setAdapter(recyclerCapitulos);
        }
    }
}