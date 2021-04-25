package es.iesoretania.entertainmentlounge.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class ProfileFragment extends Fragment {
    Button btnLogOut;

    FirebaseAuth fAuth;

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

        Toast.makeText(getContext(), "EMAIL LOGUEADO: " + UserData.USER_EMAIL, Toast.LENGTH_SHORT).show();

        btnLogOut = view.findViewById(R.id.btnLogOut);

        fAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(v.getContext(), fAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                fAuth.signOut();
                UserData.USER_EMAIL = null;
                getActivity().onBackPressed();
            }
        });
    }
}