package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AuthFragment extends Fragment {
    Button btnEntrar;
    EditText etEmail, etPassword;
    TextView tvRegister;
    ProgressBar loadingLogin;
    CheckBox chkbxMantenerSesion;
    ImageView imgLogoApp;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
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
        if (FirebaseAuth.getInstance().getCurrentUser() != null && UserData.ID_USER_DB != null) {
            Log.d("USER:LOGGED", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Log.d("USER:LOGGED", FirebaseAuth.getInstance().getCurrentUser().getUid());
            Navigation.findNavController(view).navigate(R.id.action_nav_login_to_profileFragment);
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null && UserData.ID_USER_DB == null) {
            FirebaseAuth.getInstance().signOut();
        } // else if (CHECKBOX MARCADO) {}
        btnEntrar = view.findViewById(R.id.btnEntrar);
        btnEntrar.setEnabled(true);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etEmail.setText("jerohg98@gmail.com");
        etPassword.setText("jero1234");
        tvRegister = view.findViewById(R.id.tvRegister);
        chkbxMantenerSesion = view.findViewById(R.id.chkbxMantenerSesion);
        loadingLogin = view.findViewById(R.id.loadingLogin);
        imgLogoApp = view.findViewById(R.id.imgLogoApp);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setup();


        /*
             SUBIR FOTOS A FIREBASE STORAGE Y PODER UTILIZARLAS POR NOMBRE //
             ORDENAR LAS FOTOS POR CARPETAS
             - FOTOS DE PERFIL
             - FOTOS PARA LAS SERIES

        firebaseStorage = FirebaseStorage.getInstance("gs://connectfirebasetest-9a345.appspot.com");
        StorageReference storageReference = firebaseStorage.getReference().child("Pie1900x600.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(imgLogoApp);
            }
        });
        */
        Glide.with(getContext()).load("https://upload.wikimedia.org/wikipedia/commons/8/85/Logo-Test.png").into(imgLogoApp);
    }

    private void setup() {
        loadingLogin.setEnabled(false);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_login_to_nav_register);
            }
        });
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLogin.setEnabled(true);
                loadingLogin.setVisibility(View.VISIBLE);
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    fAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                btnEntrar.setEnabled(false);
                                // if (task.getResult().getUser().isEmailVerified()) {
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
                                // } else {
                                //  Toast.makeText(getContext(), "No has verificado el correo. Veríficalo antes de poder acceder a la aplicación", Toast.LENGTH_SHORT).show();
                                // }
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
        btnEntrar.setEnabled(true);
    }
}