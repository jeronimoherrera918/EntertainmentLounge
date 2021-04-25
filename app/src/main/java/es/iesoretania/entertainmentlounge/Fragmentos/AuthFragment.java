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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.iesoretania.entertainmentlounge.Clases.UserData;
import es.iesoretania.entertainmentlounge.MainActivity;
import es.iesoretania.entertainmentlounge.R;

public class AuthFragment extends Fragment {
    Button btnEntrar;
    EditText etEmail, etPassword;
    TextView tvRegister;
    ProgressBar loadingLogin;

    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setDrawer_Locked();
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (UserData.USER_EMAIL != null) {
            Navigation.findNavController(view).navigate(R.id.action_nav_login_to_profileFragment);
        }

        btnEntrar = view.findViewById(R.id.btnEntrar);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);

        etEmail.setText("correo@correo.com");
        etPassword.setText("12345678");

        tvRegister = view.findViewById(R.id.tvRegister);

        loadingLogin = view.findViewById(R.id.loadingLogin);

        fAuth = FirebaseAuth.getInstance();

        setup();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getActivity()).setDrawer_Unlocked();
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
                                Log.d("REGISTER_USER", task.getResult().getUser().getEmail());
                                UserData.USER_EMAIL = task.getResult().getUser().getEmail();
                                Navigation.findNavController(v).navigate(R.id.action_nav_login_to_profileFragment);
                                loadingLogin.setVisibility(View.INVISIBLE);
                            } else {
                                showAlert();
                            }
                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), "Introduce los datos antes de intentar entrar", Toast.LENGTH_SHORT).show();
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