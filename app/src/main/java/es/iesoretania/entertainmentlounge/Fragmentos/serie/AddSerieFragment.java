package es.iesoretania.entertainmentlounge.Fragmentos.serie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Capitulo;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Temporada;
import es.iesoretania.entertainmentlounge.R;

public class AddSerieFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Temporada> temporadas = new ArrayList<>();
    private List<Capitulo> capitulos = new ArrayList<>();
    private List<String> plataformas = new ArrayList<>();
    private Button btnAddSerieAgregar, btnAddSerieAgregarTemporada, btnAddSerieAgregarCapitulo, btnAddSerieAgregarPlataforma;
    private EditText etAddSerieNombre, etAddSerieGenero, etAddSerieDescripcion, etAddSerieNombreCapitulo, etAddSeriePlataforma;
    private Serie serie;
    private DocumentReference newRef;
    private String newKey;

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
        newRef = db.collection("series").document();
        newKey = newRef.getId();
        btnAddSerieAgregar = view.findViewById(R.id.btnAddSerieAgregar);
        btnAddSerieAgregarTemporada = view.findViewById(R.id.btnAddSerieAgregarTemporada);
        btnAddSerieAgregarCapitulo = view.findViewById(R.id.btnAddSerieAgregarCapitulo);
        btnAddSerieAgregarPlataforma = view.findViewById(R.id.btnAddSerieAgregarPlataforma);
        etAddSerieNombre = view.findViewById(R.id.etAddSerieNombre);
        etAddSerieGenero = view.findViewById(R.id.etAddSerieGenero);
        etAddSerieDescripcion = view.findViewById(R.id.etAddSerieDescripcion);
        etAddSerieNombreCapitulo = view.findViewById(R.id.etAddSerieNombreCapitulo);
        etAddSeriePlataforma = view.findViewById(R.id.etAddSeriePlataforma);

        serie = new Serie();

        btnAddSerieAgregarPlataforma.setOnClickListener(v -> plataformas.add(etAddSeriePlataforma.toString()));
        btnAddSerieAgregarCapitulo.setOnClickListener(v -> {
            Capitulo capitulo = new Capitulo(etAddSerieNombreCapitulo.getText().toString(), 0.0);
            System.out.println(capitulo.getNombre());
            capitulos.add(capitulo);
            etAddSerieNombreCapitulo.setText("");
        });
        btnAddSerieAgregarTemporada.setOnClickListener(v -> {
            Temporada temporada = new Temporada();
            temporada.getCapitulos().addAll(capitulos);
            temporada.setPuntuacion(0.0);
            temporada.setPuntuacionTotal(0.0);
            serie.getTemporadas().add(temporada);
            capitulos.clear();
        });
        btnAddSerieAgregar.setOnClickListener(v -> {
            serie.setId_serie(newKey);
            serie.setNombre(etAddSerieNombre.getText().toString());
            serie.setGenero(etAddSerieGenero.getText().toString());
            serie.setDescripcion(etAddSerieDescripcion.getText().toString());
            serie.setPuntuacion(0.0);
            serie.setPuntuacionTotal(0.0);
            serie.setnVotos(0);
            serie.getPlataformas().addAll(plataformas);
            newRef.set(serie);
            temporadas.clear();
            getActivity().onBackPressed();
        });
    }
}