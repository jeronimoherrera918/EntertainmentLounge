package es.iesoretania.entertainmentlounge.Fragmentos;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Clases.SerieData.Serie;
import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.R;

public class ProfileFragment extends Fragment {
    TextView tvNumSeriesGuardadas, tvProfileNombre, tvProfileEmail;
    ImageView imgProfileFoto;
    Button btnAddSerie;
    FirebaseAuth fAuth;
    FirebaseFirestore db;

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
        tvNumSeriesGuardadas = view.findViewById(R.id.tvNumSeriesGuardadas);
        tvProfileNombre = view.findViewById(R.id.tvProfileNombre);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        imgProfileFoto = view.findViewById(R.id.imgProfileFoto);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //endregion

        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tvNumSeriesGuardadas.setText("Número de series guardadas: " + task.getResult().size());
                }
            }
        });

        db.collection("usuarios").document(UserData.ID_USER_DB).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Usuario usuario = task.getResult().toObject(Usuario.class);
                    tvProfileNombre.setText("Bienvenido " + usuario.getNombre_completo());
                    tvProfileEmail.setText(usuario.getEmail());
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    animation.setDuration(2000);
                    tvProfileNombre.startAnimation(animation);
                    tvProfileEmail.startAnimation(animation);
                    imgProfileFoto.startAnimation(animation);
                }
            }
        });

        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_addSerieFragment);
            }
        });
    }
}