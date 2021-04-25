package es.iesoretania.entertainmentlounge.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;

public class RegisterFragment extends Fragment {
    Button btnRegistrarse;
    EditText etPasswordRegistro, etEmailRegistro;
    FirebaseAuth fAuth;
    ProgressBar loadingRegister;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRegistrarse = view.findViewById(R.id.btnRegistrarse);

        etEmailRegistro = view.findViewById(R.id.etEmailRegistro);
        etPasswordRegistro = view.findViewById(R.id.etPasswordRegistro);

        loadingRegister = view.findViewById(R.id.loadingRegister);

        fAuth = FirebaseAuth.getInstance();

        setup();
    }

    private void setup() {
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingRegister.setVisibility(View.VISIBLE);
                if (!etEmailRegistro.getText().toString().isEmpty() && !etPasswordRegistro.getText().toString().isEmpty()) {
                    fAuth.createUserWithEmailAndPassword(etEmailRegistro.getText().toString(), etPasswordRegistro.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingRegister.setVisibility(View.INVISIBLE);
                            } else {
                                showAlert();
                            }
                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), "Introduce los datos antes de intentar entrar", Toast.LENGTH_SHORT).show();
                    loadingRegister.setVisibility(View.INVISIBLE);
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

        loadingRegister.setVisibility(View.INVISIBLE);
    }
}