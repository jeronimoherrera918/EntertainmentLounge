package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;
import pl.droidsonroids.gif.GifImageView;

public class AuthFragment extends Fragment {
    Button btnEntrar;
    EditText etEmail, etPassword;
    TextView tvRegister;
    ProgressBar loadingLogin;
    CheckBox chkbxMantenerSesion;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    GifImageView logoGif;
    FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setDrawer_Locked();
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setDrawer_Unlocked();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //region Comprobar si ha mantenido la sesión iniciada
        if (comprobarSesion() && UserData.ID_USER_DB == null) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            entrar(sharedPreferences.getString("email", "null"), sharedPreferences.getString("password", "null"));
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null && UserData.ID_USER_DB != null) {
            Navigation.findNavController(view).navigate(R.id.action_nav_login_to_profileFragment);
        } else {
            FirebaseAuth.getInstance().signOut();
        }
        //endregion

        //region Declaración de los elementos del fragmento
        btnEntrar = view.findViewById(R.id.btnEntrar);
        btnEntrar.setEnabled(true);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        tvRegister = view.findViewById(R.id.tvRegister);
        chkbxMantenerSesion = view.findViewById(R.id.chkbxMantenerSesion);
        loadingLogin = view.findViewById(R.id.loadingLogin);
        logoGif = view.findViewById(R.id.logoGif);
        //endregion

        loguearse();

    }

    private void mantenerSesion() {
        if (chkbxMantenerSesion.isChecked()) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("mantenerSesion", true);
            editor.putString("email", etEmail.getText().toString());
            editor.putString("password", etPassword.getText().toString());
            editor.apply();
        }
    }

    private boolean comprobarSesion() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("mantenerSesion", false);
    }

    private void loguearse() {
        loadingLogin.setEnabled(false);
        tvRegister.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_login_to_nav_register));

        btnEntrar.setOnClickListener(v -> {
            loadingLogin.setEnabled(true);
            loadingLogin.setVisibility(View.VISIBLE);
            if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                entrar(etEmail.getText().toString(), etPassword.getText().toString());
            } else {
                Snackbar.make(v, "Debes introducir todos los datos antes de intentar entrar", Snackbar.LENGTH_SHORT).show();
                loadingLogin.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void entrar(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loguearse -> {
            if (loguearse.isSuccessful()) {
                btnEntrar.setEnabled(false);
                // if (task.getResult().getUser().isEmailVerified()) {
                CollectionReference usuariosRef = db.collection("usuarios");
                Query query = usuariosRef.whereEqualTo("email", loguearse.getResult().getUser().getEmail());
                query.get().addOnCompleteListener(entrar -> {
                    if (entrar.isSuccessful()) {
                        for (QueryDocumentSnapshot dn : entrar.getResult()) {
                            UserData.USUARIO = dn.toObject(Usuario.class);
                            UserData.ID_USER_DB = dn.getId();
                            mantenerSesion();
                            Navigation.findNavController(getView()).navigate(R.id.action_nav_login_to_profileFragment);
                            loadingLogin.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                // } else {
                //  Toast.makeText(getContext(), "No has verificado el correo. Veríficalo antes de poder acceder a la aplicación", Toast.LENGTH_SHORT).show();
                // }
            } else {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Se ha producido un error, comprueba los datos e inténtalo de nuevo");
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        loadingLogin.setVisibility(View.INVISIBLE);
        btnEntrar.setEnabled(true);
    }
}