package es.iesoretania.entertainmentlounge.Fragmentos.acceso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.iesoretania.entertainmentlounge.Clases.Usuario;
import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;

public class RegisterFragment extends Fragment {
    private Button btnRegistrarse;
    private EditText etPasswordRegistro, etEmailRegistro, etNicknameRegistro, etNombreCompletoRegistro, etFechaRegistro;
    private FirebaseAuth fAuth;
    private ProgressBar loadingRegister;
    private FirebaseFirestore firestoredb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setDrawer_Locked();
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setDrawer_Unlocked();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Declaración de elementos del fragmento
        btnRegistrarse = view.findViewById(R.id.btnRegistrarse);
        etNicknameRegistro = view.findViewById(R.id.etNicknameRegistro);
        etEmailRegistro = view.findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = view.findViewById(R.id.etPasswordRegistro);
        etNombreCompletoRegistro = view.findViewById(R.id.etNombreCompletoRegistro);
        etFechaRegistro = view.findViewById(R.id.etFechaRegistro);
        loadingRegister = view.findViewById(R.id.loadingRegister);
        fAuth = FirebaseAuth.getInstance();
        firestoredb = FirebaseFirestore.getInstance();
        //endregion

        setup();
    }

    private void setup() {
        btnRegistrarse.setOnClickListener(v -> {
            loadingRegister.setVisibility(View.VISIBLE);
            Query query = firestoredb.collection("usuarios").whereEqualTo("nickname", etNicknameRegistro.getText().toString());
            query.get().addOnCompleteListener(userExists -> {
                if (userExists.isSuccessful()) {
                    if (userExists.getResult().size() > 0) {
                        Toast.makeText(getContext(), "El nickname elegido ya está registrado para otro usuario", Toast.LENGTH_SHORT).show();
                        loadingRegister.setVisibility(View.INVISIBLE);
                    } else if (etPasswordRegistro.getText().length() <= 8) {
                        loadingRegister.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "La contraseña debe tener mínimo una longitud de 8 caracteres", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!etEmailRegistro.getText().toString().isEmpty() && !etPasswordRegistro.getText().toString().isEmpty() && !etNicknameRegistro.getText().toString().isEmpty() && !etNombreCompletoRegistro.getText().toString().isEmpty() && !etFechaRegistro.getText().toString().isEmpty()) {
                            fAuth.createUserWithEmailAndPassword(etEmailRegistro.getText().toString(), etPasswordRegistro.getText().toString()).addOnCompleteListener(createUserPassword -> {
                                if (createUserPassword.isSuccessful()) {
                                    FirebaseUser newUser = fAuth.getCurrentUser();
                                    newUser.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Correo de verificación enviado", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Log.d("ERROR", "Ha ocurrido un error al enviar el correo de verificación" + e.getMessage()));
                                    Usuario usuario = new Usuario();
                                    usuario.setEmail(etEmailRegistro.getText().toString());
                                    usuario.setNickname(etNicknameRegistro.getText().toString());
                                    usuario.setFotoPerfil(null);
                                    usuario.setNombre_completo(etNombreCompletoRegistro.getText().toString());
                                    usuario.setFechaNacimiento(etFechaRegistro.getText().toString());
                                    usuario.setListaAmigos(new ArrayList<>());
                                    usuario.setAmigosPendientes(new ArrayList<>());
                                    firestoredb.collection("usuarios").add(usuario);
                                    loadingRegister.setVisibility(View.INVISIBLE);
                                } else {
                                    showAlert();
                                }
                            });
                        } else {
                            Toast.makeText(v.getContext(), "Introduce todos los datos antes de intentar registrarte", Toast.LENGTH_SHORT).show();
                            loadingRegister.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    showAlert();
                }
            });
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
        loadingRegister.setVisibility(View.INVISIBLE);
    }
}