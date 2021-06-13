package es.iesoretania.entertainmentlounge.Fragmentos.Pruebas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import es.iesoretania.entertainmentlounge.Actividades.PopUpComentar;
import es.iesoretania.entertainmentlounge.Adapters.RecyclerComentarios;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Comentario;
import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.R;

public class ComentariosFragment extends Fragment {
    private String keySerie;
    private RecyclerView listComentariosView;
    private FloatingActionButton fabHacerComentario;
    private FirebaseFirestore db;
    private Serie serie;
    private Integer nTemporada, nCapitulo, capVisto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_comentarios, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            ComentariosFragmentArgs comentariosFragmentArgs = ComentariosFragmentArgs.fromBundle(getArguments());
            keySerie = comentariosFragmentArgs.getKeySerie();
            nTemporada = comentariosFragmentArgs.getNTemporada();
            nCapitulo = comentariosFragmentArgs.getNCapitulo();
            capVisto = comentariosFragmentArgs.getCapVisto();
        }

        listComentariosView = view.findViewById(R.id.listComentariosView);
        fabHacerComentario = view.findViewById(R.id.fabHacerComentario);
        db = FirebaseFirestore.getInstance();

        db.collection("series").document(keySerie).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    serie = task.getResult().toObject(Serie.class);
                    mostrarComentarios();

                    fabHacerComentario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (capVisto == 1) {
                                intentComentar();
                            }
                        }
                    });
                }
            }
        });
    }

    private void mostrarComentarios() {
        List<Comentario> listaComentarios = serie.getTemporadas().get(nTemporada).getCapitulos().get(nCapitulo).getListaComentarios();
        RecyclerComentarios recyclerComentarios = new RecyclerComentarios(listaComentarios, getContext());
        listComentariosView = this.getView().findViewById(R.id.listComentariosView);
        listComentariosView.setHasFixedSize(true);
        listComentariosView.setLayoutManager(new LinearLayoutManager(listComentariosView.getContext()));
        listComentariosView.setAdapter(recyclerComentarios);
    }

    private void intentComentar() {
        Intent intent = new Intent(getContext(), PopUpComentar.class);
        Bundle bundle = new Bundle();
        bundle.putString("keySerie", serie.getId_serie());
        bundle.putInt("nCapitulo", nCapitulo);
        bundle.putInt("nTemporada", nTemporada);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}