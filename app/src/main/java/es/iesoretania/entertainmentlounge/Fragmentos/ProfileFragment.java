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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class ProfileFragment extends Fragment {
    private TextView tvProfileNombre, tvProfileEmail, tvPerfilSeriesGuardadas, tvPerfilSeriesVistas;
    private ImageView imgProfileFoto;
    private Button btnPerfilExplorar, btnPerfilMisSeries, btnPerfilDescubrir;
    private Button btnAddSerie;
    private FloatingActionButton fabEditarPerfil, fabChatear, fabAgregarAmigo;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private Integer contadorSeriesGuardadas, contadorSeriesVistas;
    private GifImageView imgLoading;
    private ProfileFragmentArgs profileFragmentArgs;
    private String keyUser;

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
        fabAgregarAmigo = view.findViewById(R.id.fabAgregarAmigo);
        tvProfileNombre = view.findViewById(R.id.tvProfileNombre);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvPerfilSeriesGuardadas = view.findViewById(R.id.tvPerfilSeriesGuardadas);
        tvPerfilSeriesVistas = view.findViewById(R.id.tvPerfilSeriesVistas);
        imgProfileFoto = view.findViewById(R.id.imgProfileFoto);
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        imgLoading = view.findViewById(R.id.imgLoading);
        //endregion

        if (getArguments() != null) {
            profileFragmentArgs = ProfileFragmentArgs.fromBundle(getArguments());
            keyUser = profileFragmentArgs.getKeyUserComment();
        } else {
            keyUser = UserData.ID_USER_DB;
        }

        db.collection("usuarios").document(keyUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Usuario usuario = task.getResult().toObject(Usuario.class);
                tvProfileNombre.setText(usuario.getNombre_completo());
                tvProfileEmail.setText(usuario.getEmail());

                db.collection("usuarios").document(keyUser).collection("series_guardadas").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        contadorSeriesGuardadas = 0;
                        contadorSeriesVistas = 0;
                        for (DocumentSnapshot dn : task1.getResult()) {
                            contadorSeriesGuardadas++;
                            if (dn.toObject(SaveSerie.class).getVistaCompleta()) {
                                contadorSeriesVistas++;
                            }
                        }
                        tvPerfilSeriesGuardadas.setText("Series guardadas: " + contadorSeriesGuardadas);
                        tvPerfilSeriesVistas.setText("Series vistas: " + contadorSeriesVistas);
                    }
                });

                StorageReference storageReference = firebaseStorage.getReference().child(usuario.getFotoPerfil());
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imgLoading.setVisibility(View.GONE);
                    Glide.with(getContext()).load(uri).into(imgProfileFoto);
                    imgProfileFoto.setVisibility(View.VISIBLE);
                });

                if (keyUser.equals(UserData.ID_USER_DB)) {
                    btnAddSerie.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_addSerieFragment));
                    btnPerfilExplorar.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_verSeries));
                    btnPerfilMisSeries.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_misSeries));
                    btnPerfilDescubrir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_recomendaciones));
                    fabEditarPerfil.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_modificarDatos));
                    fabChatear.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_chats));
                } else {
                    btnAddSerie.setVisibility(View.GONE);
                    btnPerfilExplorar.setVisibility(View.GONE);
                    btnPerfilMisSeries.setVisibility(View.GONE);
                    btnPerfilDescubrir.setVisibility(View.GONE);
                    fabEditarPerfil.setVisibility(View.GONE);
                    fabChatear.setVisibility(View.GONE);
                    fabAgregarAmigo.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}