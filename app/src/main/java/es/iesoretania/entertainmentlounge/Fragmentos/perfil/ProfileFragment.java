package es.iesoretania.entertainmentlounge.Fragmentos.perfil;

import android.content.Intent;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import es.iesoretania.entertainmentlounge.Actividades.PopUpAceptarAmigos;
import es.iesoretania.entertainmentlounge.Actividades.PopUpComentar;
import es.iesoretania.entertainmentlounge.Clases.SaveSerieData.SaveSerie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.Fragmentos.perfil.ProfileFragmentArgs;
import es.iesoretania.entertainmentlounge.Fragmentos.perfil.ProfileFragmentDirections;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class ProfileFragment extends Fragment {
    private TextView tvProfileNombre, tvProfileEmail, tvPerfilSeriesGuardadas, tvPerfilSeriesVistas;
    private ImageView imgProfileFoto;
    private Button btnPerfilExplorar, btnPerfilMisSeries, btnPerfilDescubrir;
    private Button btnAddSerie;
    private FloatingActionButton fabEditarPerfil, fabChatear, fabAgregarAmigo, fabNotificacionesPendientes;
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
        fabNotificacionesPendientes = view.findViewById(R.id.fabNotificacionesPendientes);
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
            if (keyUser.equals("navDrawClick")) {
                keyUser = UserData.ID_USER_DB;
            }
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

                if (usuario.getFotoPerfil() != null) {
                    StorageReference storageReference = firebaseStorage.getReference().child(usuario.getFotoPerfil());
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imgLoading.setVisibility(View.GONE);
                        Glide.with(getContext()).load(uri).into(imgProfileFoto);
                        imgProfileFoto.setVisibility(View.VISIBLE);
                    });
                }


                if (keyUser.equals(UserData.ID_USER_DB)) {
                    btnAddSerie.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_addSerieFragment));
                    btnPerfilExplorar.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_verSeries));
                    btnPerfilMisSeries.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_misSeries));
                    btnPerfilDescubrir.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_recomendaciones));
                    fabEditarPerfil.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_modificarDatos));
//                    if (usuario.getEmail().equals("jeronimo.herrera.918@estudiante.iesoretania.es")) {
//                        btnAddSerie.setVisibility(View.VISIBLE);
//                    }
                    fabChatear.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_chats));
                    if (usuario.getAmigosPendientes().size() == 0) {
                        Snackbar.make(view, "No tienes solicitudes pendientes", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                    } else {
                        fabNotificacionesPendientes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), PopUpAceptarAmigos.class);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    btnAddSerie.setVisibility(View.GONE);
                    btnPerfilExplorar.setVisibility(View.GONE);
                    btnPerfilMisSeries.setVisibility(View.GONE);
                    btnPerfilDescubrir.setVisibility(View.GONE);
                    fabEditarPerfil.setVisibility(View.GONE);
                    fabChatear.setVisibility(View.GONE);
                    fabAgregarAmigo.setVisibility(View.VISIBLE);
                    fabNotificacionesPendientes.setVisibility(View.GONE);
                    db.collection("usuarios").document(UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Usuario user = task.getResult().toObject(Usuario.class);
                                if (user.getListaAmigos().contains(keyUser)) {
                                    fabAgregarAmigo.setImageResource(R.drawable.ic_chats);
                                }
                            }
                        }
                    });
                    fabAgregarAmigo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.collection("usuarios").document(UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Usuario user = task.getResult().toObject(Usuario.class);
                                        if (!user.getListaAmigos().contains(keyUser)) {
                                            if (!usuario.getAmigosPendientes().contains(UserData.ID_USER_DB)) {
                                                usuario.getAmigosPendientes().add(UserData.ID_USER_DB);
                                                db.collection("usuarios").document(keyUser).set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar.make(v, "Petición de amistad enviada", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Snackbar.make(v, "Ya le has enviado una petición de amistad a este usuario", Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();
                                            }
                                        } else {
                                            Navigation.findNavController(view).navigate(ProfileFragmentDirections.actionNavProfileToNavChat(keyUser, usuario.getNickname()));
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}