package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;

public class AuthFragment extends Fragment {
    Button btnEntrar;
    EditText etEmail, etPassword;
    TextView tvRegister;
    ProgressBar loadingLogin;
    CheckBox chkbxMantenerSesion;
    FirebaseAuth fAuth;
    FirebaseFirestore db;

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
        // TODO: Nota interesante: Cuando se inicia sesion en Firebase, si yo cierro la aplicación, la sesión se mantiene abierta
        // TODO: Gracias a esto, puedo poner un CheckBox que le permita al usuario mantener la sesión abierta
        if (FirebaseAuth.getInstance().getCurrentUser() != null && UserData.ID_USER_DB != null) { // Si ya hay un usuario logueado, no podrá llegar a este fragmento nunca
            Log.d("USER:LOGGED", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Log.d("USER:LOGGED", FirebaseAuth.getInstance().getCurrentUser().getUid());
            Navigation.findNavController(view).navigate(R.id.action_nav_login_to_profileFragment);
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null && UserData.ID_USER_DB == null) {
            FirebaseAuth.getInstance().signOut();
        } // else if (CHECKBOX MARCADO) {}
        btnEntrar = view.findViewById(R.id.btnEntrar);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etEmail.setText("jerohg98@gmail.com");
        etPassword.setText("jero1234");
        tvRegister = view.findViewById(R.id.tvRegister);
        chkbxMantenerSesion = view.findViewById(R.id.chkbxMantenerSesion);
        loadingLogin = view.findViewById(R.id.loadingLogin);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setup();
    }

    private void setup() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_login_to_nav_register);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLogin.setVisibility(View.VISIBLE);
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    fAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                CollectionReference usuariosRef = db.collection("usuarios");
                                Query query = usuariosRef.whereEqualTo("email", task.getResult().getUser().getEmail());
                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot dn : task.getResult()) {
                                                Usuario usuario = dn.toObject(Usuario.class);
                                                UserData.USER_EMAIL = usuario.getEmail();
                                                UserData.NICKNAME = usuario.getNickname();
                                                UserData.FULL_NAME = usuario.getNombre_completo();
                                                UserData.DATE = usuario.getFechaNacimiento();
                                                UserData.PROFILE_PIC = usuario.getFotoPerfil();
                                                UserData.ID_USER_DB = dn.getId();
                                                Navigation.findNavController(v).navigate(R.id.action_nav_login_to_profileFragment);
                                                loadingLogin.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    }
                                });
                            } else {
                                showAlert();
                            }
                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), "Debes introducir todos los datos antes de intentar entrar", Toast.LENGTH_SHORT).show();
                    loadingLogin.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Se ha producido un error, comprueba los datos e inténtalo de nuevo");
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        loadingLogin.setVisibility(View.INVISIBLE);
    }
}