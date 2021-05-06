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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class ProfileFragment extends Fragment {
    TextView tvNumSeriesGuardadas;
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
        btnAddSerie = view.findViewById(R.id.btnAddSerie);
        tvNumSeriesGuardadas = view.findViewById(R.id.tvNumSeriesGuardadas);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(UserData.ID_USER_DB).collection("series_guardadas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tvNumSeriesGuardadas.setText("Número de series guardadas: " + task.getResult().size());
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