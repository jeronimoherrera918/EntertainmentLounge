package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.R;

public class FirstFragment extends Fragment {
    Button btnRegistrarse, btnEntrar;
    EditText etEmail, etPassword;

    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRegistrarse = view.findViewById(R.id.btnRegistrarse);
        btnEntrar = view.findViewById(R.id.btnEntrar);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        fAuth = FirebaseAuth.getInstance();

        setup();
    }

    private void setup() {
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    fAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("REGISTER_USER", task.getResult().getUser().getEmail());
                            } else {
                                showAlert();
                            }
                        }
                    });
                }
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    fAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("REGISTER_USER", task.getResult().getUser().getEmail());
                            } else {
                                showAlert();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showAlert() {
        Log.d("REGISTER_ERROR", "ERROR AL REGISTRAR AL USUARIO");
    }
}