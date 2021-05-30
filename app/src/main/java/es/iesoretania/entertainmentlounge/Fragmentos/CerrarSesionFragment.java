package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.R;

public class CerrarSesionFragment extends Fragment {
    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAlert(view);

    }

    private void showAlert(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Seguro que quieres cerrar sesión?");
        builder.setTitle("Cerrar sesión");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (dialog, which) -> getActivity().onBackPressed());
        builder.setPositiveButton("Si", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            UserData.USUARIO.setEmail(null);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("mantenerSesion", false);
            editor.putString("email", null);
            editor.putString("password", null);
            editor.commit();
            Navigation.findNavController(v).navigate(R.id.action_nav_cerrarSesion_to_nav_login);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}