package es.iesoretania.entertainmentlounge.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class ProfileFragment extends Fragment {
    private TextView tvProfileNombre, tvProfileEmail, tvPerfilSeriesGuardadas, tvPerfilSeriesVistas;
    private ImageView imgProfileFoto;
    private Button btnPerfilExplorar, btnPerfilMisSeries, btnPerfilDescubrir;
    private Button btnAddSerie;
    private FloatingActionButton fabEditarPerfil, fabChatear;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private Integer contadorSeriesGuardadas, contadorSeriesVistas;
    private GifImageView imgLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Declaración de los elementos del fragmento
        btnAddSerie = view.findViewById(R.id.btnAddSerie);
        btnPerfilExplorar = view.findViewById(R.id.btnPerfilExplorar);
        btnPerfilMisSeries = view.findViewById(R.id.btnPerfilMisSeries);
        btnPerfilDescubrir = view.findViewById(R.id.btnPerfilDescubrir);
        fabEditarPerfil = view.findViewById(R.id.fabEditarPerfil);
        fabChatear = view.findViewById(R.id.fabChatear);
        tvProfileNombre = view.findViewById(R.id.tvProfileNombre);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvPerfilSeriesGuardadas = view.findViewById(R.id.tvPerfilSeriesGuardadas);
        tvPerfilSeriesVistas = view.findViewById(R.id.tvPerfilSeriesVistas);
        imgProfileFoto = view.findViewById(R.id.imgProfileFoto);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        imgLoading = view.findViewById(R.id.imgLoading);
        //endregion

        tvProfileNombre.setText(UserData.USUARIO.getNombre_completo());
        tvProfileEmail.setText(UserData.USUARIO.getEmail());

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                contadorSeriesGuardadas = 0;
                contadorSeriesVistas = 0;
                for (DocumentSnapshot dn : task.getResult()) {
                    contadorSeriesGuardadas++;
                    if (dn.toObject(SaveSerie.class).getVistaCompleta()) {
                        contadorSeriesVistas++;
                    }
                }
                tvPerfilSeriesGuardadas.setText("Series guardadas: " + contadorSeriesGuardadas);
                tvPerfilSeriesVistas.setText("Series vistas: " + contadorSeriesVistas);
            }
        });

        StorageReference storageReference = firebaseStorage.getReference().child(UserData.USUARIO.getFotoPerfil());
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            imgLoading.setVisibility(View.GONE);
            Glide.with(getContext()).load(uri).into(imgProfileFoto);
            imgProfileFoto.setVisibility(View.VISIBLE);
        });

        btnAddSerie.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_addSerieFragment));
        btnPerfilExplorar.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_verSeries));
        btnPerfilMisSeries.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_misSeries));
        btnPerfilDescubrir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_recomendaciones));
        fabEditarPerfil.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_modificarDatos));
        fabChatear.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_chats));
    }
}