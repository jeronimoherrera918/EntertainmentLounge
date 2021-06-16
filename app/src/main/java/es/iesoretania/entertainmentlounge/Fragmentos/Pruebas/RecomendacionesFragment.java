package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

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
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import es.iesoretania.entertainmentlounge.Adapters.RecyclerSeries;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class RecomendacionesFragment extends Fragment {
    private FirebaseFirestore db;
    private Button btnRecomiendame, btnVerRecomendaciones;
    private RecyclerView listSeriesRecomendaciones;
    private int nSeriesGuardadas;
    private GifImageView loadingRecomendaciones;
    private List<Serie> listaSeries;
    private List<SaveSerie> listaSeriesGuardadas;
    private int contador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recomendaciones, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        btnRecomiendame = view.findViewById(R.id.btnRecomiendame);
        btnVerRecomendaciones = view.findViewById(R.id.btnVerRecomendaciones);
        listSeriesRecomendaciones = view.findViewById(R.id.listSeriesRecomendaciones);
        loadingRecomendaciones = view.findViewById(R.id.imgLoading3);

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(seriesGuardadas -> {
            if (seriesGuardadas.isSuccessful()) {
                if (seriesGuardadas.getResult().getDocuments().size() >= 0) {
                    listaSeriesGuardadas = new ArrayList<>();
                    for (DocumentSnapshot dn : seriesGuardadas.getResult()) {
                        listaSeriesGuardadas.add(dn.toObject(SaveSerie.class));
                    }
                    nSeriesGuardadas = listaSeriesGuardadas.size();
                    listaSeries = new ArrayList<>();
                    for (SaveSerie sv : listaSeriesGuardadas) {
                        db.collection("series").whereEqualTo("id_serie", sv.getId_serie()).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Serie serie = task.getResult().getDocuments().get(0).toObject(Serie.class);
                                listaSeries.add(serie);
                                nSeriesGuardadas--;
                                if (nSeriesGuardadas == 0) {
                                    activarBoton();
                                }
                            }
                        });
                    }
                } else {
                    Snackbar.make(view, "Debes haber guardado un mínimo de 5 series para poder empezar a tener recomendaciones", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                }
            }
        });
    }

    private void activarBoton() {
        btnRecomiendame.setEnabled(true);
        btnRecomiendame.setOnClickListener(v -> {
            loadingRecomendaciones.setVisibility(View.VISIBLE);
            btnRecomiendame.animate().translationX(btnRecomiendame.getWidth() + 300).setDuration(1000L);
            btnVerRecomendaciones.setVisibility(View.VISIBLE);

            List<String> generos = new ArrayList<>();
            for (Serie serie : listaSeries) {
                generos.add(serie.getGenero());
            }
            generos.add("Bélica");
            generos.add("Alacaca");
            generos.add("Alacaca");
            generos.add("Bélica");

            Collections.sort(generos);

            List<String> listaGeneros = generos;
            HashSet<String> uniqueGeneros = new HashSet<>(listaGeneros);
            System.out.println(uniqueGeneros);

            List<Integer> numGeneros = new ArrayList<>();
            int contador = 0;
            for (String gen : uniqueGeneros) {
                numGeneros.add(0);
                for (String gen2 : generos) {
                    if (gen.equals(gen2)) {
                        numGeneros.set(contador, numGeneros.get(contador) + 1);
                    }
                }
                contador++;
            }
            System.out.println(numGeneros);

            int nMax = Collections.max(numGeneros);
            String generoMasVisto = null;
            contador = 0;
            for (String gen : uniqueGeneros) {
                if (contador == numGeneros.indexOf(nMax)) {
                    generoMasVisto = gen;
                    break;
                }
                contador++;
            }
            System.out.println(generoMasVisto);

            List<Serie> listaSeriesGenero = new ArrayList<>();
            db.collection("series").whereEqualTo("genero", generoMasVisto).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot dn : task.getResult()) {
                        Serie s = dn.toObject(Serie.class);
                        for (SaveSerie sv : listaSeriesGuardadas) {
                            if (!sv.getId_serie().equals(s.getId_serie())) {
                                listaSeriesGenero.add(s);
                                if (listaSeriesGenero.size() != 3) {
                                    break;
                                }
                            }
                        }

                        if (listaSeriesGenero.size() != 3) {
                            numGeneros.remove(numGeneros.indexOf(nMax));

                        }
                    }
                    loadingRecomendaciones.animate().translationX(loadingRecomendaciones.getWidth() + 300).setDuration(1000L);
                    btnVerRecomendaciones.setEnabled(true);
                    btnVerRecomendaciones.setOnClickListener(v1 -> mostrarSerie(listaSeriesGenero));
                }
            });
        });
    }

    private void mostrarSerie(List<Serie> listaSeriesGenero) {
        listSeriesRecomendaciones.setVisibility(View.VISIBLE);
        btnVerRecomendaciones.animate().translationX(btnVerRecomendaciones.getWidth() + 300).setDuration(1000L);

        RecyclerSeries recyclerSeries = new RecyclerSeries(listaSeriesGenero, listSeriesRecomendaciones.getContext());
        recyclerSeries.setOnItemClickListener((position, v) -> Navigation.findNavController(v).navigate(RecomendacionesFragmentDirections.actionNavRecomendacionesToNavSerie(listaSeriesGenero.get(position).getId_serie())));

        listSeriesRecomendaciones.setHasFixedSize(true);
        listSeriesRecomendaciones.setLayoutManager(new LinearLayoutManager(listSeriesRecomendaciones.getContext()));
        listSeriesRecomendaciones.setAdapter(recyclerSeries);
    }
}
