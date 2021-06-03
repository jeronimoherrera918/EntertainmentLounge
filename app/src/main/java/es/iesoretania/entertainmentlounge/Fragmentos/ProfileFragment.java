package es.iesoretania.entertainmentlounge.Fragmentos;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class ProfileFragment extends Fragment {
    TextView tvNumSeriesGuardadas, tvProfileNombre, tvProfileEmail;
    ImageView imgProfileFoto;
    Button btnAddSerie;
    FloatingActionButton fabEditarPerfil;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    FirebaseStorage firebaseStorage;

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
        fabEditarPerfil = view.findViewById(R.id.fabEditarPerfil);
        tvNumSeriesGuardadas = view.findViewById(R.id.tvNumSeriesGuardadas);
        tvProfileNombre = view.findViewById(R.id.tvProfileNombre);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        imgProfileFoto = view.findViewById(R.id.imgProfileFoto);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        //endregion

        tvProfileNombre.setText(UserData.USUARIO.getNombre_completo());
        tvProfileEmail.setText(UserData.USUARIO.getEmail());

        StorageReference storageReference = firebaseStorage.getReference().child(UserData.USUARIO.getFotoPerfil());
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getContext()).load(uri).into(imgProfileFoto));

        btnAddSerie.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_addSerieFragment));
        fabEditarPerfil.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_modificarDatos));
    }
}