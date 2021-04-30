package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.R;

public class AddSerieFragment extends Fragment {
    FirebaseFirestore db;
    List<Temporada> temporadas = new ArrayList<>();
    List<Capitulo> capitulos = new ArrayList<>();
    List<String> plataformas = new ArrayList<>();
    Button btnAddSerieAgregar, btnAddSerieAgregarTemporada, btnAddSerieAgregarCapitulo;
    EditText etAddSerieNombre, etAddSerieGenero, etAddSerieDescripcion, etAddSerieNombreCapitulo;
    Serie serie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_serie, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        btnAddSerieAgregar = view.findViewById(R.id.btnAddSerieAgregar);
        btnAddSerieAgregarTemporada = view.findViewById(R.id.btnAddSerieAgregarTemporada);
        btnAddSerieAgregarCapitulo = view.findViewById(R.id.btnAddSerieAgregarCapitulo);
        etAddSerieNombre = view.findViewById(R.id.etAddSerieNombre);
        etAddSerieGenero = view.findViewById(R.id.etAddSerieGenero);
        etAddSerieDescripcion = view.findViewById(R.id.etAddSerieDescripcion);
        etAddSerieNombreCapitulo = view.findViewById(R.id.etAddSerieNombreCapitulo);
        serie = new Serie();

        btnAddSerieAgregarCapitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Capitulo capitulo = new Capitulo();
                capitulo.setNombre(etAddSerieNombreCapitulo.getText().toString());
                capitulo.setPuntuacion(0.0);
                capitulos.add(capitulo);
            }
        });
        btnAddSerieAgregarTemporada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temporada temporada = new Temporada();
                temporada.getCapitulos().addAll(capitulos);
                serie.getTemporadas().add(temporada);
                capitulos.clear();
            }
        });
        btnAddSerieAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serie.setNombre(etAddSerieNombre.getText().toString());
                serie.setGenero(etAddSerieGenero.getText().toString());
                serie.setDescripcion(etAddSerieDescripcion.getText().toString());
                serie.setPuntuacion(0.0);
                plataformas.add("Netflix");
                serie.setPlataformas(plataformas);
                db.collection("series").add(serie);
                temporadas.clear();
                getActivity().onBackPressed();
            }
        });
    }
}